# ShadowDefend_Game

A tower defence Game Based On 
1. JAVA.
2. Maven.
3. Custom Library Bagel.

## How To Run?
1. You need to install latest [JAVA](https://www.oracle.com/java/technologies/downloads/#license-lightbox) to run the program.
1. Download from the release and run the program

<p align="left">
  <img src="image.png"  width="1000" >
</p>



## Developer Notes:
packaging:
- Now Use auto-py-to-exe

  ```pyinstaller -F -w -i icon.ico --add-data="data/*:data" ShadowDefend.py --clean```

- windows:
  ```pyinstaller -F -w -i icon.ico --add-data="data/*;data" ShadowDefend.py --clean```

- Built Path:
  ```ShadowDefend_Game/ShadowDefend_Game/Dist/ShadowDefend```

- Windows one line bat file
  ```
  %~dp0
  pyinstaller -F -w -i icon.ico texteditor.py --clean --workpath ./pybuilds/builds --distpath ./pybuilds/dists -n myApp
  ```
