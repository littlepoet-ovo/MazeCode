# MazeCode
## Main 程序入口
## Database 用户登录数据库管理类
> - link() 功能：连接数据库 返回值：200 连接成功 | -1 连接失败
> - login(username,password) [账号,密码] 功能：用户登录 返回值：200 登录成功 | -1 账号不存在 | -2 密码错误
> - registrant(username, password, money)[账号,密码,初始金额] 功能：用户注册 返回值：200 注册成功| -1 注册失败,该用户名已存在
> - getMoney() 功能：查询用户当前金币数 返回值： -1 查询失败 | -2 用户未登录 | 其他数字 查询成功，返回值即为用户的金币数
> - setMoney(money)[正数为增加金币，负数为减少金币] 返回值：200 修改成功 | -1 修改失败 | -2 改动量不能为0 | -3 当前用户未登录
> -  getUsername() 功能：获取用户名 若未登录返回-1
> - saveGameData(useTime,useMoney,level)[花费时间(单位：秒),花费的金币,关卡等级] 功能：玩家通关之后保存数据，用于之后rank排名 返回值：201 打破纪录 | 200 成功 | -1 失败 | -2 未登录
> - getGameRank(level)[关卡等级] 功能：提供关卡等级后返回排序后的名单 返回值：用&分割同一组的不同数据，用|分割不同组， 样例数据：用户名&花费时间(秒)&使用金币&通关日期
> - getMoneyRank() 功能：返回根据拥有金币数量高低的排名 返回值：用户名&金币数 eg:gaodie&600|wangleru&600|zijie&300
## RankUI 显示Rank榜的可视化界面
## LoginUI 登录界面
## RegistrantUI 注册界面
## ModeSelUI 游戏模式选择界面
## GameUI 游戏主体UI界面
## MapPanel 游戏地图加载和操作游戏
## MapUtil 创建游戏地图、读取本地地图数据
## ModeTrickUI 关卡选择界面
## GameRunningData 游戏面板和游戏主体数据交互的接口
