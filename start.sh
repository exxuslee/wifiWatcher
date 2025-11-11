#!/usr/bin/bash

# 🚀 Разблокируем Wi-Fi и отключаем энергосбережение
echo "Initializing Wi-Fi..."
/sbin/iwconfig wlan0 power off
/sbin/ifconfig wlan0 up

# Ждём немного, чтобы адаптер успел проснуться
sleep 5

# 💡 Запускаем Kotlin Wi-Fi Watcher
echo "Starting WiFiWatcher..."
/usr/bin/java -jar /home/exxus/wifiWatcher/wifiWatcher-all.jar
