from machine import Pin, I2C
from lcd_api import LcdApi
from pico_i2c_lcd import I2cLcd
from mfrc522 import MFRC522
import utime
import json
import usocket as socket
import ussl
import ubinascii  # Για Base64 κωδικοποίηση
import network   # handles connecting to WiFi
import urequests # handles making and servicing network requests

wlan = network.WLAN(network.STA_IF)
wlan.active(True)

ssid = 'Huawei_fRST6B'  
password = 'zCymSD7q'

wlan.connect(ssid, password)
# Έλεγχος για επιτυχή σύνδεση
while not wlan.isconnected():
    print("waiting.....")

print("Wi-Fi connected, IP address:", wlan.ifconfig())

# Κωδικοποίηση όνομα χρήστη και κωδικού σε Base64
username = 'Bill'
password = '210123456789bill!'
auth = ubinascii.b2a_base64(username + ':' + password).strip()

# Σύνδεση σε HTTPS server με SSL
addr_info = socket.getaddrinfo('192.168.1.12', 8443)
addr = addr_info[0][-1]
print("Connecting to server at:", addr)
s = socket.socket()
try:
    s.connect(addr)
    print("Socket connected to server.")
except Exception as e:
    print("Failed to connect socket:", e)

# Wrap the socket with SSL
try:
    ssl_socket = ussl.wrap_socket(s)
    print("Socket wrapped with SSL.")
except Exception as e:
    print("Failed to wrap socket with SSL:", e)
mac_address = ubinascii.hexlify(network.WLAN().config('mac'),':').decode()
headers = {
    "X-Device-MAC": mac_address,
    "Authorization": "Basic " + auth.decode()
}

# Στέλνουμε το PUT αίτημα με τη MAC διεύθυνση στο header
response = urequests.put(
    'https://192.168.1.12:8443/api/basket/confirm/4546/1', 
    headers=headers  # Χρησιμοποιούμε το header με την MAC διεύθυνση και το Authorization
)
if response.status_code == 200:
    print("Basket 4546 confirmed.")
else:
    print(f"Failed to confirm basket 4546: {response.text}")
    
response = urequests.get(
    'https://192.168.1.12:8443/api/productsIDs', 
    headers=headers  # Χρησιμοποιούμε το header με την MAC διεύθυνση και το Authorization
)
if response.status_code == 200:
    productIDs = response.json()
    print(productIDs)
else:
    print(f"Failed to take the Ids: {response.text}")
    
response = urequests.get(
    'https://192.168.1.12:8443/api/productIds/4546', 
    headers=headers  # Χρησιμοποιούμε το header με την MAC διεύθυνση και το Authorization
)
if response.status_code == 200:
    basketProductIds = response.json()
    print(basketProductIds)
else:
    print(f"Failed to take the Ids: {response.text}")

I2C_ADDR = 0x27
I2C_NUM_ROWS = 2
I2C_NUM_COLS = 16
i2c = I2C(0, sda=machine.Pin(0), scl=machine.Pin(1), freq=400000)
lcd = I2cLcd(i2c, I2C_ADDR, I2C_NUM_ROWS, I2C_NUM_COLS)

reader = MFRC522(spi_id=0,sck=6,miso=4,mosi=7,cs=5,rst=22)
lcd.clear()
lcd.backlight_on()
lcd.putstr("Let's Shopping")

while True:
    
    reader.init()
    (stat, tag_type) = reader.request(reader.REQIDL)
    if stat == reader.OK:
        (stat, uid) = reader.SelectTagSN()
        if stat == reader.OK:
            lcd.backlight_on()
            card = int.from_bytes(bytes(uid),"little",False)
            print("CARD ID: "+str(card))
            if str(card) in map(str, productIDs):
                if str(card) in map(str, basketProductIds):
                    response = urequests.delete(
                        'https://192.168.1.12:8443/api/basket/4546/'+str(card), 
                        headers=headers  # Χρησιμοποιούμε το header με την MAC διεύθυνση και το Authorization
                    )
                    print("Status Code:", response.status_code)
                    print("Response Text:", response.text)
                    print("Response Headers:", response.headers)

                    # Βρες την τελευταία τελεία και απομόνωσέ την
                    data = response.text.split(".",1)  # Βρες την τελευταία τελεία
                    product_name = data[0]  # Όνομα προϊόντος
                    product_price = data[1]  # Τιμή + $

                    # Καθαρισμός και προετοιμασία της LCD
                    lcd.clear()

                    lcd.putstr("Product Removed")
                    lcd.move_to(0, 1)  # Μετακίνηση στη δεύτερη γραμμή
                    lcd.putstr(product_name[:16])  # Εμφάνιση έως 16 χαρακτήρες

                    # Παύση για ομορφιά
                    utime.sleep(2)

                    # Εναλλαγή για εμφάνιση της τιμής
                    lcd.clear()
                    lcd.putstr("Price:")
                    lcd.move_to(0, 1)
                    lcd.putstr(product_price)

                    response = urequests.put(
                        'https://192.168.1.12:8443/api/basket/setTotalCost/4546/'+str(card)+'/1', 
                        headers=headers  # Χρησιμοποιούμε το header με την MAC διεύθυνση και το Authorization
                    )
                    print("Status Code:", response.status_code)
                    print("Response Text:", response.text)
                    print("Response Headers:", response.headers)
                    lcd.clear()
                    lcd.putstr("Basket Cost:")
                    lcd.move_to(0, 1)  # Μετακίνηση στη δεύτερη γραμμή
                    lcd.putstr(response.text[:16])  # Εμφάνιση έως 16 χαρακτήρες
                    

                else:
                    response = urequests.post(
                        'https://192.168.1.12:8443/api/basket/4546/'+str(card), 
                        headers=headers  # Χρησιμοποιούμε το header με την MAC διεύθυνση και το Authorization
                    )
                    print("Status Code:", response.status_code)
                    print("Response Text:", response.text)
                    print("Response Headers:", response.headers)
                    # Βρες την τελευταία τελεία και απομόνωσέ την
                    data = response.text.split(".",1)  # Βρες την τελευταία τελεία
                    product_name = data[0]  # Όνομα προϊόντος
                    product_price = data[1]  # Τιμή + $

                    # Καθαρισμός και προετοιμασία της LCD
                    lcd.clear()

                    lcd.putstr("Product Added")
                    lcd.move_to(0, 1)  # Μετακίνηση στη δεύτερη γραμμή
                    lcd.putstr(product_name[:16])  # Εμφάνιση έως 16 χαρακτήρες

                    # Παύση για ομορφιά
                    utime.sleep(2)

                    # Εναλλαγή για εμφάνιση της τιμής
                    lcd.clear()
                    lcd.putstr("Price:")
                    lcd.move_to(0, 1)
                    lcd.putstr(product_price)
                    
                    response = urequests.put(
                        'https://192.168.1.12:8443/api/basket/setTotalCost/4546/'+str(card)+'/0', 
                        headers=headers  # Χρησιμοποιούμε το header με την MAC διεύθυνση και το Authorization
                    )
                    print("Status Code:", response.status_code)
                    print("Response Text:", response.text)
                    print("Response Headers:", response.headers)
                    
                    lcd.clear()
                    lcd.putstr("Basket Cost:")
                    lcd.move_to(0, 1)  # Μετακίνηση στη δεύτερη γραμμή
                    lcd.putstr(response.text[:16])  # Εμφάνιση έως 16 χαρακτήρες


                response = urequests.get(
                        'https://192.168.1.12:8443/api/productIds/4546', 
                        headers=headers  # Χρησιμοποιούμε το header με την MAC διεύθυνση και το Authorization
                    )
                if response.status_code == 200:
                    basketProductIds = response.json()
                    print(basketProductIds)
                else:
                    print(f"Failed to take the Ids: {response.text}")
                    
                response.close()
            else:
                lcd.clear()
                lcd.putstr("Wrong Product")
            
    utime.sleep_ms(2000) 
utime.sleep_ms(500)






 
 
 

