# ShadowDefend_Game

A Defender Game Based On JAVA.

Using Maven.
Using custom Library, Bagel.

Simply Download source code and run using IntelliJ IDE

<p align="left">
  <img src="image.png"  width="1000" >
</p>


packaging:

pyinstaller -F -w -i icon.ico --add-data="data/*:data" ShadowDefend.py --clean

windows:
pyinstaller -F -w -i icon.ico --add-data="data/*;data" ShadowDefend.py --clean

Path:
ShadowDefend_Game/ShadowDefend_Game/Dist/ShadowDefend

%~dp0
pyinstaller -F -w -i icon.ico texteditor.py --clean --workpath ./pybuilds/builds --distpath ./pybuilds/dists -n myApp