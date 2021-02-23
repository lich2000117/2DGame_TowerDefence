import subprocess
import sys
import os
import time
from tkinter import *  # GUI module



file_path = 'data/bagel.jar'



## A class for application windows
class MY_WINDOW():
    def __init__(self,init_window_name):
        self.init_window_name = init_window_name


    # Initialize windows
    def set_initial_window(self):
        #窗口
        self.init_window_name.title("ShadoeDefend塔防  -- by LCH")     
        self.init_window_name.geometry('400x280+400+280')             #290 160为窗口大小，+10 +10 定义窗口弹出时的默认展示位置
        #self.init_window_name.resizable(False, False)
        #标签
        self.title_label = Label(self.init_window_name, text="ShadowDefend")
        self.title_label.grid(row=0, column=1)
        #文本框
        
        #self.log_data_Text = Text(self.init_window_name, width=65, height=9) # 日志框
        #self.log_data_Text.grid(row=13, column=0, columnspan=10)
        #按钮
        self.start_button = Button(self.init_window_name, text="开始游戏", bg="brown1", width=15,command=lambda : self.run_game(file_path)) # 调用内部方法 加()为直接调用
        self.start_button.grid(row=2, column=0)
        self.install_java_button = Button(self.init_window_name, text="安装必备文件", bg="brown1", width=15,command=lambda : self.openNewWindow()) # 调用内部方法 加()为直接调用
        self.install_java_button.grid(row=2, column=2)
        self.init_window_name.grid_rowconfigure(1, minsize=40)  # Spacing 
       
    #获取当前时间
    def get_current_time(self):
        current_time = time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time()))
        return current_time

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
        return subprocess.call(['java', '-jar', file_path])

    # function to open a new window  
    # on a button click 
    def openNewWindow(self,): 
        
        # Toplevel object which will  
        # be treated as a new window 
        newWindow = Toplevel(self.init_window_name) 
    
        # sets the title of the 
        # Toplevel widget 
        newWindow.title("New Window") 
    
        # sets the geometry of toplevel 
        newWindow.geometry("200x200") 
    
        # A Label widget to show in toplevel 
        Label(newWindow,  
            text ="This is a new window").pack() 
    

    




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
    #run_game('data/bagel.jar')
    start_my_app()