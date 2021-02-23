%~dp0
pyinstaller -F -w -i icon.ico --add-data="data/*;data" ShadowDefend.py --clean