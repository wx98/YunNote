# YunNote 作者：王旭

## 这是一个基于 Android 和 MySql 的云笔记本程序。


### 实现功能:   
  1. **用户的注册登录退出模块**
  2. **用户信息的查找更改**操作
  3. **笔记列表的查看**操作
  4. **笔记列表的删改**操作
### 本软件的编写环境：
	Android Studio 3.2.1
	Gradle Version：4.6
	Android Pulgin Version：3.2.1
	Java Version : 1.8.0_152
	Android Sdk Version：API 28: Android 9.0 (Pie)   
![请重新加载](/MdImage/ApkEnvironmentImage.png "软件图标")
### 项目结构
>YunNote   
>>+app   
>>>lib   
>>>>[mysql-connector-java-5.1.30-bin.jar](app/libs/mysql-connector-java-5.1.30-bin.jar) (JDBC驱动)
>>
>>src   
>>>main   
>>>>java   
>>>>>cn 
>>>>>>wxxwwx98
>>>>>>>mytestapp
>>>>>>>>[AddNoteActivity.java](app/src/main/java/cn/wxxwwx98/mytestapp/AddNoteActivity.java)(添加笔记逻辑代码)   
>>>>>>>>[DBService.java](app/src/main/java/cn/wxxwwx98/mytestapp/DBService.java)(数据库基本操作类)    
>>>>>>>>[FirstActivity.java](app/src/main/java/cn/wxxwwx98/mytestapp/FirstActivity.java)(启动界面代码)    
>>>>>>>>[LoginActivity.java](app/src/main/java/cn/wxxwwx98/mytestapp/LoginActivity.java)(登录代码代码)    
>>>>>>>>[MainActivity.java](app/src/main/java/cn/wxxwwx98/mytestapp/MainActivity.java)(程序主界面代码)    
>>>>>>>>[MyListAdapter.java](app/src/main/java/cn/wxxwwx98/mytestapp/MyListAdapter.java)(笔记更改逻辑代码)    
>>>>>>>>[MyListView.java](app/src/main/java/cn/wxxwwx98/mytestapp/MyListView.java)(自定义控件完成下拉刷新功能)    
>>>>>>>>[RegisterActivity.java](app/src/main/java/cn/wxxwwx98/mytestapp/RegisterActivity.java)(注册界面代码)    
>>>>>>>>[StartActivity.java](app/src/main/java/cn/wxxwwx98/mytestapp/StartActivity.java)(启动界面动画代码)   
>>>>
>>>>res  
>>>>>drawable
>>>>>>[start.xml](app/src/main/res/drawable/start.xml)(软件图标) 
>>>>>
>>>>>layout
>>>>>>[activity_add_note.xml](app/src/main/res/layout/activity_add_note.xml)(添加笔记界面布局代码)   
>>>>>>[activity_first.xml](app/src/main/res/layout/activity_first.xml)(启动界面之后界面布局代码)    
>>>>>>[activity_login.xml](app/src/main/res/layout/activity_login.xml)(登录界面布局代码)    
>>>>>>[activity_main.xml](app/src/main/res/layout/activity_main.xml)(主界面布局代码)    
>>>>>>[activity_register.xml](app/src/main/res/layout/activity_register.xml)(注册界面布局代码)    
>>>>>>[activity_start.xml](app/src/main/res/layout/activity_start.xml)(启动动画界面布局代码)     
>>>>>>[layout_datetime.xml](app/src/main/res/layout/layout_datetime.xml)(日期选择窗口布局代码)      
>>>>>>[layout_header.xml](app/src/main/res/layout/layout_header.xml)(自定义刷新下拉控件布局代码)     
>>>>>>[layout_list.xml](app/src/main/res/layout/layout_list.xml)(笔记列表布局代码)    
>>>>>>[layout_tab1.xml](app/src/main/res/layout/layout_tab1.xml)(TabHost1布局代码)    
>>>>>>[layout_tab2.xml](app/src/main/res/layout/layout_tab2.xml)(TabHost2布局代码)      
>>>>>>[listview_item.xml](app/src/main/res/layout/listview_item.xml)(右下角的添加按键布局代码)    
>>>      
>>>[AndroidManifest.xml](app/src/main/AndroidManifest.xml)(应用程序的信息描述文件)
### 数据库文件及ER图
#### 数据库创建文件→[test.sql](test.sql)   
#### 数据库ER图   
![请重新加载](/MdImage/DbERImage.png "数据库ER图")
### 项目截图
#### APP图标   
![请重新加载](/MdImage/IconImage.png "APP图标")   
#### 启动界面
![请重新加载](/MdImage/LayoutStartImage1.png "启动界面")![请重新加载](/MdImage/LayoutStartImage2.png "启动界面")   
#### 注册界面
#### 注册界面
![请重新加载](/MdImage/LayoutRegisterImage.png "注册界面")   
#### 登录界面
![请重新加载](/MdImage/LayoutLoginImage.png "登录界面")   
#### 笔记列表界面
![请重新加载](/MdImage/LayoutTabHost1Image.png "笔记列表界面")   
#### 用户资料界面
![请重新加载](/MdImage/LayoutTabHost2Image.png "用户资料界面")   

