﻿DROP TABLE IF EXISTS category;
CREATE TABLE category (id INTEGER, pid INTEGER, name TEXT);
INSERT INTO `category` VALUES (1,NULL,'餐饮美食');
INSERT INTO `category` VALUES (2,NULL,'休闲娱乐');
INSERT INTO `category` VALUES (3,NULL,'丽人');
INSERT INTO `category` VALUES (4,NULL,'购物');
INSERT INTO `category` VALUES (5,NULL,'摄影写真');
INSERT INTO `category` VALUES (6,NULL,'旅游酒店');
INSERT INTO `category` VALUES (7,NULL,'生活服务');
INSERT INTO `category` VALUES (0,NULL,'其它');
INSERT INTO `category` VALUES (107,1,'快餐小吃');
INSERT INTO `category` VALUES (106,1,'自助');
INSERT INTO `category` VALUES (105,1,'火锅烧烤');
INSERT INTO `category` VALUES (101,1,'北京菜');
INSERT INTO `category` VALUES (111,1,'川菜');
INSERT INTO `category` VALUES (112,1,'湘菜');
INSERT INTO `category` VALUES (114,1,'粤菜');
INSERT INTO `category` VALUES (115,1,'江浙菜');
INSERT INTO `category` VALUES (123,1,'素菜');
INSERT INTO `category` VALUES (104,1,'海鲜');
INSERT INTO `category` VALUES (122,1,'东南亚菜');
INSERT INTO `category` VALUES (102,1,'日韩亚系');
INSERT INTO `category` VALUES (103,1,'西餐国际');
INSERT INTO `category` VALUES (108,1,'蛋糕甜品');
INSERT INTO `category` VALUES (109,1,'饮品');
INSERT INTO `category` VALUES (124,1,'更多地方菜');
INSERT INTO `category` VALUES (120,1,'其它');
INSERT INTO `category` VALUES (202,2,'电影话剧');
INSERT INTO `category` VALUES (201,2,'KTV');
INSERT INTO `category` VALUES (206,2,'酒吧');
INSERT INTO `category` VALUES (207,2,'桌面游戏');
INSERT INTO `category` VALUES (208,2,'游艺/电玩');
INSERT INTO `category` VALUES (209,2,'温泉滑雪');
INSERT INTO `category` VALUES (210,2,'足疗/按摩/洗浴');
INSERT INTO `category` VALUES (211,2,'运动健身');
INSERT INTO `category` VALUES (212,2,'台球厅');
INSERT INTO `category` VALUES (213,2,'咖啡/茶楼');
INSERT INTO `category` VALUES (220,2,'其它');
INSERT INTO `category` VALUES (301,3,'美容/SPA');
INSERT INTO `category` VALUES (302,3,'美发');
INSERT INTO `category` VALUES (305,3,'瘦身纤体');
INSERT INTO `category` VALUES (307,3,'瑜伽/舞蹈');
INSERT INTO `category` VALUES (320,3,'其它');
INSERT INTO `category` VALUES (401,4,'个护化妆');
INSERT INTO `category` VALUES (402,4,'服装鞋帽');
INSERT INTO `category` VALUES (403,4,'食品');
INSERT INTO `category` VALUES (404,4,'图书文具/玩具');
INSERT INTO `category` VALUES (405,4,'数码产品');
INSERT INTO `category` VALUES (406,4,'家居电器');
INSERT INTO `category` VALUES (410,4,'礼品箱包');
INSERT INTO `category` VALUES (420,4,'其它');
INSERT INTO `category` VALUES (501,5,'个人写真');
INSERT INTO `category` VALUES (502,5,'婚纱摄影');
INSERT INTO `category` VALUES (503,5,'儿童');
INSERT INTO `category` VALUES (520,5,'其它');
INSERT INTO `category` VALUES (601,6,'旅游');
INSERT INTO `category` VALUES (602,6,'酒店');
INSERT INTO `category` VALUES (620,6,'其它');
INSERT INTO `category` VALUES (708,7,'银行/ATM');
INSERT INTO `category` VALUES (706,7,'商场超市');
INSERT INTO `category` VALUES (703,7,'医院药店');
INSERT INTO `category` VALUES (701,7,'教育培训');
INSERT INTO `category` VALUES (702,7,'房产汽车');
INSERT INTO `category` VALUES (709,7,'加油站');
INSERT INTO `category` VALUES (720,7,'其它');