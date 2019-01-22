package com.sjw.beautifulapp.bean;

public class Index2Bean {


//    强势安利
    Index2Item1Bean item1Bean;

    //统一的单行书籍
    Index2Item2Bean item2Bean;

    //排行榜
    Index2Item3Bean item3Bean;


    /**
     * 分类
     */
    int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Index2Item1Bean getItem1Bean() {
        return item1Bean;
    }

    public void setItem1Bean(Index2Item1Bean item1Bean) {
        this.item1Bean = item1Bean;
    }

    public Index2Item2Bean getItem2Bean() {
        return item2Bean;
    }

    public void setItem2Bean(Index2Item2Bean item2Bean) {
        this.item2Bean = item2Bean;
    }

    public Index2Item3Bean getItem3Bean() {
        return item3Bean;
    }

    public void setItem3Bean(Index2Item3Bean item3Bean) {
        this.item3Bean = item3Bean;
    }
}
