<span style="font-family: 'Trebuchet MS';"><span style="font-size: xx-large;"><b>    Sakura Login</b></span>  <span style="font-size: medium;">更方便、更强大、更安全的登录插件</span></span><span style="font-family: 'Trebuchet MS';"><span style="font-size: medium;">
</span></span>
<div align="center"><span style="font-family: 'Trebuchet MS';"><img src="https://panel.tcotp.cn/cdn/42211976_p0.jpg" alt="" border="0" /></span></div>
&nbsp;

<span style="font-size: large;"><b>简介</b></span>

这是一款支持外置登录的登录插件，后端使用 PHP 进行用户验证，可扩展性极强，支持与本人的 SoraChat 网页聊天插件对接数据库，实现一个账号登录服务器所有服务（论坛、博客、启动器等等，完全没有局限）
部署这个插件之前，请先具备一些常规的电脑操作知识（不要再跑来问我 FTP 怎么上传之类的问题了...）以及足够的耐心，教程看似复杂，实际上部署起来只要几分钟的时间~

<span style="font-size: large;"><b>功能</b></span>
<ul class="litype_1" type="1">
 	<li><span style="font-family: 'Trebuchet MS';">支持开启/关闭游戏内注册</span></li>
 	<li><span style="font-family: 'Trebuchet MS';">支持开启/关闭外置登录</span></li>
 	<li><span style="font-family: 'Trebuchet MS';">可从数据库读取玩家状态，拒绝被封禁玩家登录</span></li>
 	<li><span style="font-family: 'Trebuchet MS';">密码采用 <b>AES-128-ECB</b> 加密方式储存，这也是业界公认最安全的加密方式之一</span></li>
 	<li><span style="font-family: 'Trebuchet MS';">支持服务端/网页端分离，数据库分离</span></li>
 	<li><span style="font-family: 'Trebuchet MS';">完全可修改的语言文件（这个貌似没什么用）</span></li>
 	<li><span style="font-family: 'Trebuchet MS';">外置登录协议可二次开发</span></li>
 	<li><span style="font-family: 'Trebuchet MS';"><span style="font-family: 'Trebuchet MS';">不需要输入命令，进游戏直接在聊天栏输入密码即可登录</span></span></li>
</ul>
<span style="font-family: 'Trebuchet MS';"> <span style="font-size: large;"><b>安装</b></span>
</span>

使用本插件非常简单，支持面板服（前提是服务商没有封掉所有未使用的端口），通常您只需要以下几个东西：
<ul class="litype_1" type="1">
 	<li><span style="font-family: 'Trebuchet MS';">一个 Minecraft 服务器</span></li>
 	<li><span style="font-family: 'Trebuchet MS';">一个虚拟主机或 VPS（可以和 Minecraft 服务器搭在同一台 VPS 上以达到最佳效果，验证速度更快）</span></li>
 	<li><span style="font-family: 'Trebuchet MS';"><span style="font-family: 'Trebuchet MS';">如果您想要启用外置登录，您可能需要自己开发启动器或者找人定制（233333）</span></span></li>
</ul>
<span style="font-family: 'Trebuchet MS';"><strong>以下就是部署步骤：</strong>
</span>
<ul class="litype_1" type="1">
 	<li><span style="font-family: 'Trebuchet MS';">点击下载 <a href="https://panel.tcotp.cn/cdn/SakuraLogin/?s=SakuraLogin" target="_blank" rel="noopener"><b>SakuraLogin-1.0.Release.jar</b></a></span></li>
 	<li><span style="font-family: 'Trebuchet MS';">将下载好的 SakuraLogin-1.0.Release.jar 放入服务器的 plugins 文件夹</span></li>
 	<li><span style="font-family: 'Trebuchet MS';">重启一次服务器，自动生成 SakuraLogin 文件夹，编辑里面的 config.yml</span></li>
 	<li><span style="font-family: 'Trebuchet MS';">根据配置文件内的提示修改对应配置，然后重启服务器</span></li>
 	<li><span style="font-family: 'Trebuchet MS';"><span style="font-family: 'Trebuchet MS';">点击下载 <a href="https://panel.tcotp.cn/cdn/SakuraLogin/?s=SakuraLoginBackEnd" target="_blank" rel="noopener"><b>SakuraLoginBackEnd-1.0.Release.zip</b></a> 并开始配置网页端，此处有两个分支：</span></span></li>
</ul>
<span style="font-family: 'Trebuchet MS';"><strong>1. 使用 VPS 或者独立主机部署后端</strong>
1.1. 如果是 Windows 的服务器，推荐使用 <a href="http://www.phpstudy.net/phpstudy/phpStudy20161103.zip" target="_blank" rel="noopener"><b>phpStudy 一键环境包</b></a>
1.2. 安装完 phpStudy 后，打开，单击“其它选项菜单” &gt; “网站根目录”
1.3. 解压 SakuraLoginBackEnd-1.0.Release.zip，复制 login 文件夹到网站根目录
1.4. 编辑 login/index.php，修改数据库用户名、密码、数据库名等
1.5. 保存，进入服务器试试吧
<b>————————————————————分割线——————————————————————</b>
1.6. 如果是 Linux 服务器，也可以使用 <a href="http://lamp.phpstudy.net/" target="_blank" rel="noopener"><b>phpStudy 一键环境包</b></a>
1.7. 根据提示安装完 phpStudy 后，输入 cd /phpstudy/www/ 定位到网站根目录
1.8. 输入命令 <b>wget <a href="https://panel.tcotp.cn/cdn/SakuraLogin/?s=SakuraLoginBackEnd" target="_blank" rel="noopener">https://panel.tcotp.cn/cdn/SakuraLogin/?s=SakuraLoginBackEnd</a></b> 下载后端文件
1.9. 输入命令 <b>unzip SakuraLoginBackEnd-1.0.Release.zip</b> 解压
1.10. 输入命令 <b>phpstudy start</b> 启动网站服务（已经启动可以忽略）
1.11. 输入命令 <b>vi /phpstudy/www/login/index.php</b>，按 <b>a</b>，然后修改数据库用户名，密码，数据库名
1.12. 按下 <b>Esc</b>，然后输入 <b>:wq</b> 保存并退出</span>

<span style="font-family: 'Trebuchet MS';"><strong>2. 使用虚拟主机部署后端</strong>
2.1. 打开 FTP 软件，推荐用 Windows 资源管理器自带的就行了，地址栏输入 ftp://服务器地址/ 回车
2.2. 将 <b>SakuraLoginBackEnd-1.0.Release.zip</b> 解压
2.3. 进入虚拟主机网站根目录，一般就是登陆上去以后的默认文件夹或者是 WWW、Web 文件夹
2.4. 编辑 <b>login/index.php</b>，修改数据库用户名、密码、数据库名（一般虚拟主机后台都可以看到的）
2.5. 将 <b>login</b> 文件夹上传到网站根目录（如果提示上传失败请仔细看步骤 5.2.3）
2.6. 登录游戏看看效果吧</span>
<div align="center"><span style="font-family: 'Trebuchet MS';"><img src="http://www.mcbbs.net/forum.php?mod=image&amp;aid=1186127&amp;size=300x300&amp;key=e174ca0b497b228a&amp;nocache=yes&amp;type=fixnone" alt="" width="300" border="0" /></span></div>
请注意：服务器插件配置文件里的 apiurl 就是你的 VPS 或者虚拟主机的网站地址，<b>切记结尾一定要加 <span style="color: #ff0000;">/</span></b>
类似于这样：
<div align="center"><img src="http://www.mcbbs.net/forum.php?mod=image&amp;aid=1186126&amp;size=300x300&amp;key=2bc4dffa4c21c748&amp;nocache=yes&amp;type=fixnone" alt="" width="300" border="0" /></div>
MC 服务器登录插件配置文件中的 connectpass <b>必须要和网页后端的 token 设置一致</b>，否则无法通讯（这是 API 的访问密码）。

<span style="font-size: large;"><b>外置登录</b></span>

您需要自己会写启动器（拿现成源码改也是可以的！）
首先下载这个文件：<a href="https://panel.tcotp.cn/cdn/SakuraLogin/?s=SakuraLoginClient" target="_blank" rel="noopener"><b>SakuraLoginClient-1.0.Release.jar</b></a>
把这个文件放进您的客户端 .minecraft 文件夹中然后在启动器中按照以下格式调用：
<div class="blockcode">
<blockquote>java -jar SoraLoginClient-1.0.Release.jar &lt;你的服务器IP&gt; &lt;外置登录端口&gt; &lt;玩家名字&gt; &lt;玩家密码&gt;</blockquote>
</div>
<span style="font-family: 'Trebuchet MS';">注意这里的外置登录端口<b>不是服务器的端口</b>，而是 MC 服务器登录插件配置文件里的这一项：</span>

<span style="font-family: 'Trebuchet MS';"><img class="aligncenter" src="http://www.mcbbs.net/forum.php?mod=image&amp;aid=1186129&amp;size=300x300&amp;key=37a3bce9f42da4e9&amp;nocache=yes&amp;type=fixnone" alt="" width="271" border="0" /></span>

举个例子，你的服务器IP是 123.123.123.123，设置的外置登录端口如上 6666，玩家名字 test 玩家密码 123456，那么您可以这样调用：
易语言可以这样：<span style="color: #ff0000;">运行</span> (<span style="color: #9acd32;">"java -jar .minecraft/SakuraLoginClient.jar 123.123.123.123 6666 test 123456"</span>, <span style="color: #0000ff;">假</span>, <span style="color: #9932cc;">#隐藏窗口</span>)
其他语言也差不多。

<b>给更高级的开发者：</b>
其实也可以直接和服务器建立 socket 连接，端口就是外置登录端口

然后每隔 1 秒发送一次  用户名/密码，例如 test/123456
<div align="center"><span style="font-family: 'Trebuchet MS';">如果你喜欢这个插件，欢迎给我赞助哟~</span></div>
<div align="center">支付宝 jianghao7172@tcotp.cn</div>
<div align="center"><span style="font-family: 'Trebuchet MS';">如果想要转载的话，请先通过回复告诉我哦~</span></div>
<div align="center"><span style="font-family: 'Trebuchet MS';"><img src="http://www.mcbbs.net/forum.php?mod=image&amp;aid=1186130&amp;size=300x300&amp;key=8d2903f46bf9eb8e&amp;nocache=yes&amp;type=fixnone" alt="" width="300" border="0" /></span></div>
<div align="center"><span style="font-family: 'Trebuchet MS';">※ \ 完结~撒花~ / ※</span></div>
