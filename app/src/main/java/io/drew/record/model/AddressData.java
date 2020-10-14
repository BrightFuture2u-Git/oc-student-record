package io.drew.record.model;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.List;

/**
 * @Author: KKK
 * @CreateDate: 2020/9/10 10:14 AM
 */
public class AddressData implements IPickerViewData {

    /**
     * id : 2
     * name : 天津
     * parentId : 0
     * initial : t
     * initials : tj
     * pinyin : tianjin
     * extra :
     * suffix : 市
     * code : 120000
     * areaCode : 022
     * weight : 2
     * subRegion : [{"id":54,"name":"和平","parentId":2,"initial":"h","initials":"hp","pinyin":"heping","extra":"","suffix":"区","code":"120101","areaCode":"022","weight":1,"subRegion":[]},{"id":55,"name":"河东","parentId":2,"initial":"h","initials":"hd","pinyin":"hedong","extra":"","suffix":"区","code":"120102","areaCode":"022","weight":2,"subRegion":[]},{"id":56,"name":"河西","parentId":2,"initial":"h","initials":"hx","pinyin":"hexi","extra":"","suffix":"区","code":"120103","areaCode":"022","weight":3,"subRegion":[]},{"id":57,"name":"南开","parentId":2,"initial":"n","initials":"nk","pinyin":"nankai","extra":"","suffix":"区","code":"120104","areaCode":"022","weight":4,"subRegion":[]},{"id":58,"name":"河北","parentId":2,"initial":"h","initials":"hb","pinyin":"hebei","extra":"","suffix":"区","code":"120105","areaCode":"022","weight":5,"subRegion":[]},{"id":59,"name":"红桥","parentId":2,"initial":"h","initials":"hq","pinyin":"hongqiao","extra":"","suffix":"区","code":"120106","areaCode":"022","weight":6,"subRegion":[]},{"id":60,"name":"东丽","parentId":2,"initial":"d","initials":"dl","pinyin":"dongli","extra":"","suffix":"区","code":"120110","areaCode":"022","weight":7,"subRegion":[]},{"id":61,"name":"西青","parentId":2,"initial":"x","initials":"xq","pinyin":"xiqing","extra":"","suffix":"区","code":"120111","areaCode":"022","weight":8,"subRegion":[]},{"id":62,"name":"津南","parentId":2,"initial":"j","initials":"jn","pinyin":"jinnan","extra":"","suffix":"区","code":"120112","areaCode":"022","weight":9,"subRegion":[]},{"id":63,"name":"北辰","parentId":2,"initial":"b","initials":"bc","pinyin":"beichen","extra":"","suffix":"区","code":"120113","areaCode":"022","weight":10,"subRegion":[]},{"id":64,"name":"武清","parentId":2,"initial":"w","initials":"wq","pinyin":"wuqing","extra":"","suffix":"区","code":"120114","areaCode":"022","weight":11,"subRegion":[]},{"id":65,"name":"宝坻","parentId":2,"initial":"b","initials":"bc","pinyin":"baochi","extra":"","suffix":"区","code":"120115","areaCode":"022","weight":12,"subRegion":[]},{"id":66,"name":"滨海新区","parentId":2,"initial":"b","initials":"bhxq","pinyin":"binhaixinqu","extra":"","suffix":"","code":"120116","areaCode":"022","weight":13,"subRegion":[]},{"id":67,"name":"宁河","parentId":2,"initial":"n","initials":"nh","pinyin":"ninghe","extra":"","suffix":"区","code":"120117","areaCode":"022","weight":14,"subRegion":[]},{"id":68,"name":"蓟州","parentId":2,"initial":"j","initials":"jz","pinyin":"jizhou","extra":"","suffix":"区","code":"120119","areaCode":"022","weight":16,"subRegion":[]},{"id":69,"name":"静海","parentId":2,"initial":"j","initials":"jh","pinyin":"jinghai","extra":"","suffix":"区","code":"120118","areaCode":"022","weight":15,"subRegion":[]}]
     */

    private int id;
    private String name;
    private int parentId;
    private String initial;
    private String initials;
    private String pinyin;
    private String extra;
    private String suffix;
    private String code;
    private String areaCode;
    private int weight;
    private List<SubRegionBean> subRegion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public List<SubRegionBean> getSubRegion() {
        return subRegion;
    }

    public void setSubRegion(List<SubRegionBean> subRegion) {
        this.subRegion = subRegion;
    }

    @Override
    public String getPickerViewText() {
        return name;
    }

    public static class SubRegionBean {
        /**
         * id : 54
         * name : 和平
         * parentId : 2
         * initial : h
         * initials : hp
         * pinyin : heping
         * extra :
         * suffix : 区
         * code : 120101
         * areaCode : 022
         * weight : 1
         * subRegion : []
         */

        private int id;
        private String name;
        private int parentId;
        private String initial;
        private String initials;
        private String pinyin;
        private String extra;
        private String suffix;
        private String code;
        private String areaCode;
        private int weight;
        private List<SubRegionBean> subRegion;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public String getInitial() {
            return initial;
        }

        public void setInitial(String initial) {
            this.initial = initial;
        }

        public String getInitials() {
            return initials;
        }

        public void setInitials(String initials) {
            this.initials = initials;
        }

        public String getPinyin() {
            return pinyin;
        }

        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }

        public String getExtra() {
            return extra;
        }

        public void setExtra(String extra) {
            this.extra = extra;
        }

        public String getSuffix() {
            return suffix;
        }

        public void setSuffix(String suffix) {
            this.suffix = suffix;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public List<SubRegionBean> getSubRegion() {
            return subRegion;
        }

        public void setSubRegion(List<SubRegionBean> subRegion) {
            this.subRegion = subRegion;
        }
    }
}
