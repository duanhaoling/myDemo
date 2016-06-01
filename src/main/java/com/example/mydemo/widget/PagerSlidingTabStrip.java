package com.example.mydemo.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.example.mydemo.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ldh on 2016/5/4 0004.
 */
public class PagerSlidingTabStrip extends HorizontalScrollView implements View.OnClickListener {


    private Drawable slidingBlockDrawable;  //滑块
    private boolean allowWidthFull;    // 内容宽度无法充满时，允许自动调整Item的宽度以充满
    private boolean disableViewPager;  // 禁用ViewPager
    private ViewGroup tabsLayout; //标题项布局
    private List<View> tabViews;
    private int currentPosition;  //当前位置
    private ViewPager viewPager;
    private int lastOffset;
    private int lastScrollX = 0;
    private float currentPositionOffset; // 当前位置偏移量
    private boolean start;
    private View currentSelectedTabView; //当前标题页
    private OnClickTabListener onClickTabListener;
    private ViewPager.OnPageChangeListener onPageChangeListener; //绑定ViewPager的切换监听器
    private OnPagerChangeLis listener;

    public PagerSlidingTabStrip(Context context) {
        this(context, null);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @SuppressLint("NewApi")
    public PagerSlidingTabStrip(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setHorizontalScrollBarEnabled(false); //隐藏横向滑动条

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PagerSlidingTabStrip);
            if (typedArray != null) {
                slidingBlockDrawable = typedArray.getDrawable(R.styleable.PagerSlidingTabStrip_slidingBlock);
                allowWidthFull = typedArray.getBoolean(R.styleable.PagerSlidingTabStrip_allowWidthFull, false);
                disableViewPager = typedArray.getBoolean(R.styleable.PagerSlidingTabStrip_disableViewPager, false);
                typedArray.recycle();
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!allowWidthFull) {
            return;
        }
        ViewGroup tabsLayout = getTabsLayout();
        if (tabsLayout == null || tabsLayout.getMeasuredWidth() >= getMeasuredWidth()) {
            return;
        }
        if (tabsLayout.getChildCount() <= 0) {
            return;
        }

        if (tabViews == null) {
            tabViews = new ArrayList<>();
        } else {
            tabViews.clear();
        }
        for (int i = 0; i < tabsLayout.getChildCount(); i++) {
            tabViews.add(tabsLayout.getChildAt(i));
        }
        adjustChildWidthWithParent(
                tabViews,
                getMeasuredWidth() - tabsLayout.getPaddingLeft() - tabsLayout.getPaddingRight(),
                widthMeasureSpec,
                heightMeasureSpec
        );
        //TODO
//        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }

    /**
     * 调整views集合中的View，让所有View的宽度加起来正好等于parentViewWidth
     *
     * @param views                   子View集合
     * @param parentViewWidth         父View的宽度
     * @param parentWidthMeasureSpec  父View的宽度规则
     * @param parentHeightMeasureSpec 父View的高度规则
     */
    private void adjustChildWidthWithParent(List<View> views, int parentViewWidth,
                                            int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        //先去掉所有子View的外边距
        for (View view : views) {
            if (view.getLayoutParams() instanceof MarginLayoutParams) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
                parentViewWidth -= lp.leftMargin + lp.rightMargin;
            }
        }

        // 去掉宽度大于平均宽度的View后再次计算平均宽度
        int bigTabCount = views.size();
        int averageWidth = parentViewWidth / views.size();
        while (true) {
            Iterator<View> iterator = views.iterator();
            while (iterator.hasNext()) {
                View view = iterator.next();
                if (view.getMeasuredWidth() > averageWidth) {
                    parentViewWidth -= view.getMeasuredWidth();
                    bigTabCount--;
                    iterator.remove();
                }
            }
            averageWidth = parentViewWidth / bigTabCount;
            boolean end = true;
            for (View view : views) {
                if (view.getMeasuredWidth() > averageWidth) {
                    end = false;
                }
            }
            if (end) {
                break;
            }
        }

        // 修改宽度小于新的平均宽度的View的宽度
        for (View view : views) {
            if (view.getMeasuredWidth() < averageWidth) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
                lp.width = averageWidth;
                view.setLayoutParams(lp);
                //再次测量让新宽度生效
                if (lp instanceof MarginLayoutParams) {
                    measureChildWithMargins(view, parentWidthMeasureSpec, 0, parentHeightMeasureSpec, 0);
                } else {
                    measureChild(view,parentWidthMeasureSpec,parentHeightMeasureSpec);
                }
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        ViewGroup tabsLayout = getTabsLayout();
        if (tabsLayout != null) {
            //初始化滑块位置及选中状态
            currentPosition = viewPager != null ? viewPager.getCurrentItem() : 0;
            if (!disableViewPager) {
                //如果没有禁用viewPager
                scrollToChild(currentPosition, 0);//移动滑块到指定位置
                selectTab(currentPosition);
            }

            // 给每一个tab设置点击事件，当点击的时候切换Pager
            for (int w = 0; w < tabsLayout.getChildCount(); w++) {
                View itemView = tabsLayout.getChildAt(w);
                itemView.setTag(w);
                itemView.setOnClickListener(this);
            }
        }
    }

    /**
     * 选中指定位置的TAB
     */
    private void selectTab(int currentSelectedPosition) {
        ViewGroup tabsLayout = getTabsLayout();
        if (currentSelectedPosition > -1 && tabsLayout != null && currentSelectedPosition < tabsLayout.getChildCount()) {
            if (currentSelectedTabView != null) {
                currentSelectedTabView.setSelected(false);
            }
            currentSelectedTabView = tabsLayout.getChildAt(currentSelectedPosition);
            if (currentSelectedTabView != null) {
                currentSelectedTabView.setSelected(true);
            }
        }
    }

    /**
     * 移动滑块,滚动到指定的位置
     */
    private void scrollToChild(int position, int offset) {
        ViewGroup tabsLayout = getTabsLayout();
        if (tabsLayout != null && tabsLayout.getChildCount() > 0 && position < tabsLayout.getChildCount()) {
            View view = tabsLayout.getChildAt(position);
            if (view != null) {
                //计算新的X坐标
                int newScrollX = view.getLeft() + offset;
                if (position > 0 || offset > 0) {
                    newScrollX -= 240 - getOffset(view.getWidth()) / 2;
                }

                // 如果同上一次X坐标不一样就执行滚动
                if (newScrollX != lastScrollX) {
                    lastScrollX = newScrollX;
                    scrollTo(newScrollX,0);
                }
            }
        }
    }

    /**
     * 获取偏移量
     * @param newOffset
     * @return
     */
    private int getOffset(int newOffset) {
        if (lastOffset < newOffset) {
            if (start) {
                lastOffset += 1;
                return lastOffset;
            } else {
                start = true;
                lastOffset += 1;
                return lastOffset;
            }
        }
        if (lastOffset > newOffset) {
            if (start) {
                lastOffset -= 1;
                return lastOffset;
            } else {
                start = true;
                lastOffset -= 1;
                return lastOffset;
            }
        } else {
            start = true;
            lastOffset = newOffset;
            return lastOffset;
        }
    }

    private ViewGroup getTabsLayout() {
        // 如果为null，新建。如果不为null，直接返回
        if (tabsLayout == null) {
            if (getChildCount() > 0) {
                tabsLayout = (ViewGroup) getChildAt(0);
            } else {
                removeAllViews();
                tabsLayout = new LinearLayout(getContext());
                addView(tabsLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
            }

        }
        return tabsLayout;
    }

    @Override
    public void onClick(View v) {
        int index = (int) v.getTag();
        if (onClickTabListener != null) {
            onClickTabListener.onClickTab(v,index);
        }
        if (viewPager != null) {
            viewPager.setCurrentItem(index,true);
        }
    }

    /**
     * Tab点击监听器
     *
     * @author xiaopan
     *
     */
    public interface OnClickTabListener{
        public void onClickTab(View tab, int index);
    }

    /**
     * 设置Tab点击监听器
     *
     * @param onClickTabListener
     *            Tab点击监听器
     */
    public void setOnClickTabListener(OnClickTabListener onClickTabListener) {
        this.onClickTabListener = onClickTabListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (disableViewPager) return;
        /* 绘制滑块*/
        View currentTab = tabsLayout.getChildAt(currentPosition);
        if (currentTab != null) {
           float slidingBlockLeft = currentTab.getLeft();
           float slidingBlockRight = currentTab.getRight();
            if (currentPositionOffset > 0f && currentPosition < tabsLayout.getChildCount() - 1) {
                View nextTab = tabsLayout.getChildAt(currentPosition + 1);
                if (nextTab != null) {
                    final float nextTabLeft = nextTab.getLeft();
                    final float nextTabright = nextTab.getRight();
                    slidingBlockLeft = (currentPositionOffset * nextTabLeft
                            + (1 - currentPositionOffset) * slidingBlockLeft);
                    slidingBlockRight = (currentPositionOffset * nextTabright
                            + (1 - currentPositionOffset) * slidingBlockRight);
                }
            }
            slidingBlockDrawable.setBounds((int)slidingBlockLeft, 0, (int) slidingBlockRight, 0);
            slidingBlockDrawable.draw(canvas);
        }
    }

    /**
     * 添加Tab
     */
    public void addTab(View tabView, int index) {
        if (tabView != null) {
            getTabsLayout().addView(tabView,index);
        }
        requestLayout();
    }
    /**
     * 添加Tab
     */
    public void addTab(View tabView) {
        addTab(tabView, -1);
    }
    /**
     * 添加Tab
     */
    public void addTab(List<View> tabViews) {
        if (tabViews != null) {
            for (View view : tabViews) {
                getTabsLayout().addView(view);
            }
            requestLayout();
        }
    }
    /**
     * 添加Tab
     *
     * @param tabViews
     *            可以一次添加多个Tab
     */
    public void addTab(View... tabViews) {
        if (tabViews != null) {
            for (View view : tabViews) {
                getTabsLayout().addView(view);
            }
            requestLayout();
        }
    }

    /**
     * 移除一个Tab
     *
     * @param index
     */
    public void removeTab(int index) {
        removeTab(index, 1);
    }

    /**
     * 移除Tab
     *
     * @param start 开始位置
     * @param count     移除的数量
     */
    public void removeTab(int start, int count) {
        int tabCount = getTabCount();

        if (start < 0 || start > tabCount) {
            start = 0;
        }
        if (count < 0 || count > tabCount) {
            count = 1;
        }
        if (start + count > tabCount) {
            count = tabCount = start;
        }
        getTabsLayout().removeViews(start, count);
        requestLayout();
    }

    /**
     * 移除所有
     */
    public void removeAllTab() {
        getTabsLayout().removeAllViews();
        requestLayout();
    }

    public View getChildAt(int idx) {
        return getTabsLayout().getChildAt(idx);
    }



    /**
     * 设置ViewPager
     *
     * @param viewPager
     *            ViewPager
     */

    public void setViewPager(ViewPager viewPager) {
        if (disableViewPager) {
            return;
        }
        this.viewPager = viewPager;
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ViewGroup tabsLayout = getTabsLayout();
                if (position < tabsLayout.getChildCount()) {
                    View view = tabsLayout.getChildAt(position);
                    if (view != null) {
                        currentPosition = position;
                        currentPositionOffset = positionOffset;
                        scrollToChild(position, (int) (positionOffset * view.getWidth()));
                        invalidate();
                    }
                }
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageScrolled(position,positionOffset,positionOffsetPixels);
                }

            }

            @Override
            public void onPageSelected(int position) {
                selectTab(position);
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageSelected(position);
                }
                if (listener != null) {
                    listener.onChanged(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageScrollStateChanged(state);
                }
            }
        });

        requestLayout();
    }

    /**
     * 设置不使用ViewPager
     *
     * @param disableViewPager
     *            不使用ViewPager
     */
    public void setDisableViewPager(boolean disableViewPager) {
        this.disableViewPager = disableViewPager;
        if (viewPager != null) {
            viewPager.setOnPageChangeListener(onPageChangeListener);
            viewPager = null;
        }
        requestLayout();
    }

    /**
     * 设置Page切换监听器
     *
     * @param onPageChangeListener
     *            Page切换监听器
     */
    public void setOnPageChangeListener(
            ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    /**
     * 设置是否充满屏幕
     *
     * @param allowWidthFull true：当内容的宽度无法充满屏幕时，自动调整每一个Item的宽度以充满屏幕
     */
    public void setAllowWidthFull(boolean allowWidthFull) {
        this.allowWidthFull = allowWidthFull;
        requestLayout();
    }

    /**
     * 设置滑块图片
     */
    public void setSlidingBlockDrawable(Drawable slidingBlockDrawable) {
        this.slidingBlockDrawable = slidingBlockDrawable;
        requestLayout();
    }

    /**
     *
     * 获取Tab总数
     */
    public int getTabCount() {
        ViewGroup tabsLayout = getTabsLayout();
        return tabsLayout == null ? 0 : tabsLayout.getChildCount();
    }

    public void setOnPagerChange(OnPagerChangeLis l) {
        this.listener = l;
    }

    public interface OnPagerChangeLis {
        void onChanged(int page);
    }
}
