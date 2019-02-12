# 密码簿
账户密码备份与记录
目前密码管理变成了一个老大难的问题，本项目基于此，通过Android系统来管理自己的各种账户和密码

本项目**不记录**任何个人密码，只记录密码加密之后的密文，同时，**不需要**任何Android系统权限(包括网络权限),让人放心使用，即使**密文被盗，也无需担心**。本项目采用AES 256位加密算法加密，极大地提高了密文被解密的难度  
<img width="375" alt="主页" src="https://github.com/superLiuJing/passwordbook/blob/master/screenshot/Screenshot_1549960236.png" />

## 一、密码生成器
本项目集成了密码生成器的功能，通过不同的条件选择，随机生成密码并提供复制到剪贴板  
<img width="375" alt="密码生成器" src="https://github.com/superLiuJing/passwordbook/blob/master/screenshot/Screenshot_1549960227.png" />

## 二、密码簿
账户密码通过列表的形式展现,由于本人秘钥太多，导致定位困难，故开发一个秘钥搜索的功能，方便用户搜索定位查找  
<img width="375" alt="密码簿" src="https://github.com/superLiuJing/passwordbook/blob/master/screenshot/Screenshot_1549960245.png" />

## 三、新增记录
新增一条账户密码记录  
<img width="375" alt="新增记录" src="https://github.com/superLiuJing/passwordbook/blob/master/screenshot/Screenshot_1549960257.png" />

## 四、导入导出
密文导入导出，提升安全性。导出时直接复制到手机剪贴板，方便快捷。  
<img width="375" alt="导入记录" src="https://github.com/superLiuJing/passwordbook/blob/master/screenshot/Screenshot_1549960264.png" />

## 五、指纹加密秘钥
由于部分同事反映，每次输入秘钥非常麻烦，但是兼顾安全性，所以推出通过指纹加密秘钥，然后将加密之后的密文保存本地，下次进入，通过指纹解密秘钥，然后进一步解密的功能。  
<img width="375" alt="指纹加密" src="https://github.com/superLiuJing/passwordbook/blob/master/screenshot/20190212164436.png" />

## 六、帮助
相关注意事项已经全部写入帮助中，可以进一步查看  
<img width="375" alt="帮助" src="https://github.com/superLiuJing/passwordbook/blob/master/screenshot/Screenshot_1549960274.png" />
