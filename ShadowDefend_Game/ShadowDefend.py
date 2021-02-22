import subprocess
import sys
import os
import time
#subprocess.call(['java', '-jar', 'bagel.jar'])

def resource_path(relative_path):
    """ Get absolute path to resource, works for dev and for PyInstaller """
    try:
        # PyInstaller creates a temp folder and stores path in _MEIPASS
        base_path = sys._MEIPASS
    except Exception:
        base_path = os.path.abspath(".")

    return os.path.join(base_path, relative_path)

def run_game(file_path):
    file_path = resource_path(file_path)
    print(file_path)
    input("press to continue")
    subprocess.call(['java', '-jar', file_path])

if __name__ == '__main__':
    run_game('data/bagel.jar')