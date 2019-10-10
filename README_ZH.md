![标题](https://github.com/mCyp/Orient-Ui/blob/master/picture/cover.png)

<h1 align="center">Orient - UI</h1>
<div align="center">
  分享平时工作中一些有趣的UI</br>
<img src = "https://api.bintray.com/packages/jiewang19951030/Maven/Orient-Ui/images/download.svg"/> <img src="https://img.shields.io/badge/license-Apache2.0-green.svg" style="" /> <a href="https://codebeat.co/projects/github-com-mcyp-orient-ui-master"><img alt="codebeat badge" src="https://codebeat.co/badges/33618fe7-81fe-4d7f-ac59-054a8c62a556" /></a>



</div>

[English](https://github.com/mCyp/Orient-Ui/blob/master/README.md)|中文

## 💫 功能

#### 两侧布局 - DoubleSideLayout

借助`RecyclerView`实现以下布局。

<img width="200" height="400" src="https://github.com/mCyp/Orient-Ui/blob/master/picture/%E4%B8%A4%E4%BE%A7%E5%B8%83%E5%B1%80.png" alt="S90929-10290486"  style="float:left;" />

#### 时间轴 - TimeLine

**样式由自己定制的时间轴**。

![timeline](https://github.com/mCyp/Orient-Ui/blob/master/picture/Timeline.png)

#### 网格首页 - GridPage

摆脱嵌套，借助`RecyclerView`的`GridLayoutManager`自制首页。

<img width="200" height="400" src="https://github.com/mCyp/Orient-Ui/blob/master/picture/GridPage.png" alt="GridPage" style="float:left;" />

#### 状态视图 - EmptyView

自由切换数据的`空`、`加载`、`错误`和`显示`的状态。

<img src="https://github.com/mCyp/Orient-Ui/blob/master/picture/EmptyView.png" alt="EmptyView" style="zoom:50%;" />

#### 表格 - Table

- [ ] TODO 

## 📖 依赖和文档

####  添加依赖

```groovy
implementation 'com.orient:Orient-Ui:1.0.0'
```

#### [使用说明](https://github.com/mCyp/Orient-Ui/blob/master/doc/Document.md)

####  Demo下载

- [apk](https://github.com/mCyp/Orient-Ui/blob/master/apk/orient-ui.apk?raw=true)

## ⌨️ 技术反馈

如果您发现了什么问题，欢迎提出`Issue`，或者使用其他方式联系我：

- 【QQ】：200522649
- 【Email】：jiewang19951030@gmail.com

## 🦸‍♂️ 感谢

- [Qiujuer](https://github.com/qiujuer)：**状态视图**最早来自Qiuejuer老师的慕课教学课程[《手把手开发一个完整即时通讯App》](https://coding.imooc.com/learn/list/100.html)，不过并没有公开的库使用**状态视图**，所以我稍加修改，分享了出来。

- [AVLoadingIndicatorView](https://github.com/81813780/AVLoadingIndicatorView)：状态布局中的动画来自该库，为了在`Orient-UI`中避免依赖其他库，所以`Loading`部分直接从该库中复制而来，特此说明。
- [阿里图标库-旅游](https://www.iconfont.cn/collections/detail?cid=18705)：**状态视图**中的采用的图标来自该库，这个图标库看上去很舒服，很赞！