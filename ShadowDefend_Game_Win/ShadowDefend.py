# Windows Win10
# Python Packaging system to run java jar programme.
# Simple User Interface
# Programmed by Chenghao Li
# github: lich2000117
# 24/02/2021


import subprocess
import sys
import os
import time
import webbrowser
from tkinter import *  # GUI module
from tkinter.ttk import Progressbar, Style
from time import sleep
import random


# The game file's location
file_path = 'data/bagel.jar'



## A class for application windows
class MY_WINDOW():
    def __init__(self,init_window_name):
        self.init_window_name = init_window_name


    # Initialize windows
    def set_initial_window(self):
        #窗口
        self.init_window_name.title("ShadoeDefend塔防  -- by LCH")     
        #self.init_window_name.geometry('400x280+400+280')             #290 160为窗口大小，+10 +10 定义窗口弹出时的默认展示位置
        self.init_window_name.resizable(False, False)
        # Set windows to center
        self.window_to_center(self.init_window_name)
 
        #标签
        self.title_label = Label(self.init_window_name, text="ShadowDefend")
        self.title_label.grid(row=0, column=2)
        
        #self.log_data_Text = Text(self.init_window_name, width=65, height=9) # 日志框
        #self.log_data_Text.grid(row=13, column=0, columnspan=10)
        #按钮
        self.start_button = Button(self.init_window_name, text="开始游戏", bg="lightcyan", width=15,command=lambda : self.selectDifficulty(file_path)) # 调用内部方法 加()为直接调用
        self.start_button.grid(row=2, column=1)
        self.install_java_button = Button(self.init_window_name, text="安装必备文件", bg="lightcyan", width=15,command=lambda : self.moreHelpWindow()) # 调用内部方法 加()为直接调用
        self.install_java_button.grid(row=2, column=3)

        # Spacing 
        self.init_window_name.grid_rowconfigure(0, minsize=40)  #Row
        self.init_window_name.grid_rowconfigure(1, minsize=20)  
        self.init_window_name.grid_rowconfigure(3, minsize=60)
        
        self.init_window_name.grid_columnconfigure(0, minsize=80)
        self.init_window_name.grid_columnconfigure(1, minsize=1)  #Column
        self.init_window_name.grid_columnconfigure(2, minsize=1)
        self.init_window_name.grid_columnconfigure(3, minsize=1)
        self.init_window_name.grid_columnconfigure(4, minsize=50)

        #footer
        self.title_label = Label(self.init_window_name, text="Java Application, Created by LCH.")
        self.title_label.grid(row=3, column=2)
       
    #获取当前时间
    def get_current_time(self):
        current_time = time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time()))
        return current_time

    #Set a window to center, tk_window is tk_window = TK()
    def window_to_center(self, tk_window):
        # Set windows to center
        # Gets both half the screen width/height and window width/height
        positionRight = int(tk_window.winfo_screenwidth()/2 - tk_window.winfo_reqwidth()/2)
        positionDown = int(tk_window.winfo_screenheight()/2 - tk_window.winfo_reqheight()/2)
        # Positions the window in the center of the page.
        tk_window.geometry("+{}+{}".format(positionRight+tk_window.width/2, positionDown+tk_window.height/2))
        
    #日志动态打印
    def write_log_to_Text(self,logmsg):
        global LOG_LINE_NUM
        current_time = self.get_current_time()
        logmsg_in = str(current_time) +" " + str(logmsg) + "\n"   #换行
        # 删除之前的记录
        if LOG_LINE_NUM <= 7:
            self.log_data_Text.insert(END, logmsg_in)
            LOG_LINE_NUM = LOG_LINE_NUM + 1
        else:
            self.log_data_Text.delete(1.0,2.0)
            self.log_data_Text.insert(END, logmsg_in)

    def resource_path(self, relative_path):
        """ Get absolute path to resource, works for dev and for PyInstaller """
        try:
            # PyInstaller creates a temp folder and stores path in _MEIPASS
            base_path = sys._MEIPASS
        except Exception:
            base_path = os.path.abspath(".")
        return os.path.join(base_path, relative_path)

    # Function that runs game directly using command line
    def run_game(self, file_path):
        file_path = self.resource_path(file_path)
        subprocess.call(['javaw', '-jar', file_path])


    # function to open a new window for more Help Page
    # on a button click 
    def moreHelpWindow(self,): 
        # Toplevel object which will be treated as a new window 
        # 窗口设置
        newWindow = Toplevel(self.init_window_name) 
        newWindow.resizable(False, False)
        self.window_to_center(newWindow)
        # sets the title of the 
        # Toplevel widget 
        newWindow.title("更多帮助") 

        #Open link function
        def openLink(link):
            webbrowser.open_new(link)

        #Java Link:
        text = Label(newWindow, text="软件基于Java开发，请下载必备组件CDK运行：", height=4)
        text.pack()
        t1 = Label(newWindow, text="请根据自己的系统选择并安装JDK：")
        t1.pack()
        link1 = Label(newWindow, text="JDK Download", fg="blue", cursor="hand2")
        link1.pack()
        link1.bind("<Button-1>", lambda e: openLink("https://www.oracle.com/java/technologies/javase-jdk15-downloads.html#license-lightbox"))
        # Seperator ------------------------------------------
        sep = Label(newWindow, text="------------------More------------------", height=3)
        sep.pack()
        #Author Link:
        t2 = Label(newWindow, text="作者Author:", height=2)
        t2.pack()
        link2 = Label(newWindow, text="Github ☜ ( ͡❛ ͜ʖ ͡❛)", fg="blue", cursor="hand2")
        link2.pack()
        link2.bind("<Button-1>", lambda e: openLink("https://github.com/lich2000117"))
        #Project Link:
        t3 = Label(newWindow, text="Project Source Code:", height=3)
        t3.pack()
        link3 = Label(newWindow, text="Source Code", fg="blue", cursor="hand2")
        link3.pack()
        link3.bind("<Button-1>", lambda e: openLink("https://github.com/lich2000117/ShadowDefend_Game"))


    # function to open a new window for Selecting Difficulties
    # on a button click 
    def selectDifficulty(self,file_path): 
        # Toplevel object which will be treated as a new window 
        # 窗口设置
        self.selectWindow = Toplevel(self.init_window_name) 
        self.selectWindow.resizable(False, False)
        self.window_to_center(self.selectWindow)
        # sets the title of the 
        # Toplevel widget 
        self.selectWindow.title("开始游戏") 
        #Java Link
        text = Label(self.selectWindow, text="困难选择：", height=4, width = 60)
        text.pack()
        start_button = Button(self.selectWindow, text="Easy", bg="Lawngreen", width=25,command=lambda : self.progressbar(file_path)) # 调用内部方法 加()为直接调用
        start_button.pack()
        install_java_button = Button(self.selectWindow, text="Hard", bg="Coral", width=25,command=lambda : self.progressbar(file_path)) # 调用内部方法 加()为直接调用
        install_java_button.pack()

    def progressbar(self, file_path):
        root = Toplevel(self.init_window_name) 
        root.resizable(False, False)
        self.window_to_center(root)
        root.title("正在启动游戏...")
        s = Style(root)
        # add the label to the progressbar style
        s.layout("LabeledProgressbar",
                [('LabeledProgressbar.trough',
                {'children': [('LabeledProgressbar.pbar',
                                {'side': 'left', 'sticky': 'ns'}),
                                ("LabeledProgressbar.label",   # label inside the bar
                                {"sticky": ""})],
                'sticky': 'nswe'})])

        p = Progressbar(root, orient="horizontal", length=300,
                        style="LabeledProgressbar")
        p.pack()

        # change the text of the progressbar, 
        # the trailing spaces are here to properly center the text
        s.configure("LabeledProgressbar", text="Launching... 0 %      ")

        

        #Function that controls progress bar
        def launch_Check():
            pass_check = False
            factor = 0.01
            for i in range(1, 101):
                sleep(random.randint(1,4)*factor)
                p.step()
                if (i<10):
                    s.configure("LabeledProgressbar", text="Initializing... {0} %      ".format(i))
                # Check Java Availability at 25%
                elif (i >= 15) and (i <= 30):
                    s.configure("LabeledProgressbar", text="Checking Java Availability... {0} %      ".format(i))
                    if (i >= 27):
                        # Check if Java is installed
                        if not pass_check:
                            try:
                                subprocess.call('javaw')
                            except Exception:
                                s.configure("LabeledProgressbar", text="Error! Missing Java JDK!      ".format(i), bg='red')
                                root.update()
                                Button(root, command=self.moreHelpWindow, text="Download Java JDK",bg='red').pack()
                                return False
                elif (i>50) and (i<75):
                    factor = 0.01
                    s.configure("LabeledProgressbar", text="Loading Game... {0} %      ".format(i))
                else:
                    factor = 0.005
                    s.configure("LabeledProgressbar", text="Launching Game... {0} %      ".format(i))
                root.update()
            return True
        #If finish progress bar
        if launch_Check():
            root.destroy()
            self.selectWindow.destroy()
            self.run_game(file_path)
    





#Start APP
def start_my_app():
    #初始化Tk()
    myWindow = Tk()
    MYApp = MY_WINDOW(myWindow)
    # 设置根窗口默认属性
    MYApp.set_initial_window()
    #进入消息循环
    myWindow.mainloop()



if __name__ == '__main__':
    start_my_app()
    