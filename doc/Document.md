# Document | 文档

Orient-Ui is a Android Ui Library,it includes StatusView,TimeLine,GridPage And DoubleSideLayout now.

> Orient-UI 是一个Android Ui库，它现在主要包括状态视图、时间轴、网格首页和两侧布局。

*  [First Step | 第一步](#first-step--第一步)
*  [DoubleSideLayout | 两侧布局](#doublesidelayout--两侧布局)
	*  [Use | 使用](#use--使用)
*  [TimeLine | 时间轴](#timeline--时间轴)
	*  [Use | 使用](#use--使用-1)
	*  [# DoubleTimeLine](#-doubletimeline)
	* [# SingleTimeLine](#-singletimeline)
*  [StatusView | 状态视图](#statusview--状态视图)
	*  [Use | 使用](#use--使用-2)
*  [GridPage | 网格首页](#gridpage--网格首页)
	*  [Use | 使用](#use--使用-3)

## First Step | 第一步

Add Dependency in `build.gradle`:

> 在项目文件`build.gradle`添加依赖

```groovy
implementation 'com.orient:Orient-Ui:1.0.0'
```

## DoubleSideLayout | 两侧布局

**DoubleSideLayout** is one of `LayoutManager` in `RecyclerView`, so it bases on `RecyclerView`.

> **DoubleSideLayout**是`RecyclerView`中的`LayoutManager`，所以它需要在项目中使用`RecyclerView`。

### Use | 使用

You should assign start side when you create `DoubleSideLayoutManager`,`Left` or `Right` Side.

> 你应该指定布局开始的一侧当你创建`DoubleSideLayoutManager`的时候，左边或者右边。

```java
mRecyclerView.setLayoutManager(new DoubleSideLayoutManager(DoubleSideLayoutManager.START_LEFT));
```

## TimeLine | 时间轴

**TimeLine** is different form other kind of TimeLine,the style of TimeLine is decided by yourself! it bases on `ItemDecoration` of `ReccylelrView`.

> 不同于其他样式的时间轴，**TimeLine**的样式由你决定！它基于`RecyclerView`中的`ItemDecoration`。

### Use | 使用

you can choose: 

| name          | chooice                                                 |
| ------------- | ------------------------------------------------------- |
| TimeLineStyle | DoubleTimeLine, SingleTimeLine                          |
| LineStyle     | Divide, **Consistant**, **BeginToEnd**(like Consistant) |
| DotStyle      | **Draw**, **Resource**                                  |
| TitleStyle    | **Left**, Top                                           |

**Hint**: DoubleTimeLine only support **Bold part**.

> **提示**：DoubleTimeLine仅支持上述**加粗**部分。

### # DoubleTimeLine

e.g: TimeLineStyle-**DoubleTimeLine**; LineStyle-**BeginToEnd**; DotStyle-**Resource**; TitleStyle-**Left**;

> 举个例子：完成一个TimeLineStyle-**DoubleTimeLine**; LineStyle-**BeginToEnd**; DotStyle-**Resource**; TitleStyle-**Left**;的时间轴

<img width="200" height="400" src="https://github.com/mCyp/Orient-Ui/blob/master/picture/double_hint.jpg" alt="S90929-10290486"  style="float:left;" />

1. The data must implement `ITimeItem` interface | 数据必须继承`ITimeItem` 接口

```java
public interface ITimeItem {
	// the title you want to show
	String getTitle();
	// dot color
	int getColor();
	// or dot resource
	int getResource();
}
```

2. Create your TimeLine extends `DoubleTimeLineDecoration` | 创建你自己的`TimeLine`并继承 `DoubleTimeLineDecoration` 

```java
public class WeekPlanDTL extends DoubleTimeLineDecoration {

    public WeekPlanDTL(Config config) {
        super(config);
    }

    @Override
    protected void onDrawTitleItem(Canvas canvas, int left, int top, int right, int bottom, int centerX, int pos, boolean isLeft) {
        // Draw your title part
        // ...
    }

    @Override
    protected void onDrawDotResItem(Canvas canvas, int cx, int cy, int radius, Drawable drawable, int pos) {
        super.onDrawDotResItem(canvas, cx, cy, radius, drawable, pos);
        // draw your dot part
        // ...
    }
}
```

3. Add to  `RecyclerView` and set data | 添加进`RecyclerView`然后设置数据

```java
private TimeLine provideTimeLine(List<TimeItem> timeItems) {
	return new TimeLine.Builder(getContext(), timeItems)
	                .setTitle(Color.parseColor("#8d9ca9"), 14)
	                .setTitleStyle(TimeLine.FLAG_TITLE_TYPE_LEFT, 0)
	                .setLine(TimeLine.FLAG_LINE_BEGIN_TO_END, 60, Color.parseColor("#757575"),3)
	                .setDot(TimeLine.FLAG_DOT_RES)
	                .build(WeekPlanDTL.class);
}

// when using DoubleTimeLine
// RecyclerView must use DoubleSideLayoutManager
mRecyclerView.setLayoutManager(new DoubleSideLayoutManager(DoubleSideLayoutManager.START_LEFT));
TimeLine timeLine = provideTimeLine(timeItems);
mRecyclerView.addItemDecoration(timeLine);
```

4. Update `TimeLine` when data updates, `TimeLine` provides `Add`, `Update` And `Remve` | 当数据更新的时候记得更新`TimeLine`，`TimeLIne`提供了增加，更新和移除的接口。

### # SingleTimeLine

e.g: TimeLineStyle-**SingleTimeLine**; LineStyle-**Divide**; DotStyle-**Draw**; TitleSytle-**Top**

> 举个例子：完成一个TimeLineStyle-**SingleTimeLine**; LineStyle-**Divide**; DotStyle-**Draw**; TitleSytle-**Top**;的时间轴

<img width="200" height="400" src="https://github.com/mCyp/Orient-Ui/blob/master/picture/single_hint.jpg" alt="S90929-10290486"  style="float:left;" />

The main difference between **SingleTimeLine** and **DoubleTimeLine** is parent class, SingleTimeLine should extends **SingleTimeLineDecoration**. Otherwise, when we use `RecyclerView`, **SingleTimeLine** should use `LinearLayoutManager`, and **DoubleTimeLine** should use `DoubleSideLayoutManager`。

> 主要的区别是**SingleTimeLine** 和 **DoubleTimeLine**的父类不同，SingleTimeLine应该继承**SingleTimeLineDecoration**。除此以外，**SingleTimeLine**对应着的`RecyclerView`布局是`LinearLayoutManager`（线性布局），而 **DoubleTimeLine**对应着`DoubleSideLayoutManager`。

## StatusView | 状态视图

**StatusView** is used to express data's diffrent status, such as **Error**, **Null**, **Loading** and **Data Show**.

> 状态视图用来表达数据的不同状态，例如**错误**、**空**、**加载**和**展示数据**。

<img width="200" height="400" src="https://github.com/mCyp/Orient-Ui/blob/master/picture/StatusView.gif" alt="S90929-10290486"  style="float:left;" />

### Use | 使用

1. add into xml | 添加进xml文件中

some **attr** in **EmptyView**

> **EmptyView**中的一些属性

| attr             | explanation                          |
| ---------------- | ------------------------------------ |
| comEmptyText     | The text display in null status      |
| comErrorText     | The text displays in error status    |
| comLoadingText   | The text displays in loading status  |
| comLoadingColor  | The loading view's color             |
| comEmptyDrawable | The drawable displays in null status |
| comErrorDrawable | The drawable displays in errr status |

xml:

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context=".ui.activity.placeholder.PlaceHolderActivity">

    <android.support.v7.widget.Toolbar
        android:id="@+id/toolbar"
        android:layout_width="match_parent"
        android:background="@color/teal_300"
        app:navigationIcon="@drawable/common_ic_back"
        android:layout_height="@dimen/len_52"/>

    <!-- Data View'visibility should be gone  -->
    <TextView
        android:id="@+id/tv_name"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:text="Hello,World!"
        android:textColor="@color/textPrimary"
        android:textSize="@dimen/font_20"
        android:textStyle="bold"
        android:gravity="center"
        android:visibility="gone"/>

    <com.orient.me.widget.placeholder.EmptyView
        android:id="@+id/et_content"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>

</LinearLayout>
```

2. Get EmptyView and Bind data View | 获取`EmptyView`然后绑定数据视图

```java
// use ButterKnife
@BindView(R.id.tv_name)
TextView mContent;
@BindView(R.id.et_content)
EmptyView mEmptyView;

protected void initWidget() {
	super.initWidget();
	
	mEmptyView.bind(mContent);
}
```

3. Change Status | 切换状态

```java
// change loading status
mEmptyView.triggerLoading();
// change show data status
mEmptyView.triggerOk();
// change error status or use mEmptyView.triggerNetError();
mEmptyView.triggerError(R.string.prompt_error);
// change null status
mEmptyView.triggerEmpty();
```

## GridPage | 网格首页

**GridPage** is used to **create HomePage** and **reduce nest**, it should be Used with `RecyclerView` and `GridLayoutManager`, `GridPage` is normal `ItemDecoration`, it only provide a way to create homepage and reduce nest. 

> **GridPage** 用来构建一个首页，优点是减少嵌套，它需要组合`RecyclerView`和`GridLayoutManager`，`GridPage`是普通的，它仅仅为我们提供了一种减少嵌套创建首页的思路。

<img width="200" height="400" src="https://github.com/mCyp/Orient-Ui/blob/master/picture/GridPage.png" alt="GridPage" style="float:left;" />

### Use | 使用

see demo

> 详见Demo
