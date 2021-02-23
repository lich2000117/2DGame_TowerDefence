from tkinter import Tk
from tkinter.ttk import Progressbar, Style, Button
from time import sleep


root = Tk()
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
s.configure("LabeledProgressbar", text="0 %      ")

def fct():
    for i in range(1, 101):
        sleep(0.1)
        p.step()
        s.configure("LabeledProgressbar", text="{0} %      ".format(i))
        root.update()

Button(root, command=fct, text="launch").pack()

root.mainloop()