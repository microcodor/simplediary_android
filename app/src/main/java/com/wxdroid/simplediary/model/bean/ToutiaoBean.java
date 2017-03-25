package com.wxdroid.simplediary.model.bean;

import java.util.List;

/**
 * Created by jinchun on 2017/1/10.
 */

public class ToutiaoBean {

    /**
     * reason : 成功的返回
     * result : {"stat":"1","data":[{"uniquekey":"93c09247175d0e77a71201b10e10856c","title":"真人版《美女与野兽》公仔亮相：女主角丑哭了","date":"2017-01-10 17:28","category":"娱乐","author_name":"环球网","url":"http://mini.eastday.com/mobile/170110172822240.html","thumbnail_pic_s":"http://08.imgmini.eastday.com/mobile/20170110/20170110172822_7c5f8df6c4fd31fe215094ac3024dfc4_1_mwpm_03200403.jpeg","thumbnail_pic_s02":"http://08.imgmini.eastday.com/mobile/20170110/20170110172822_7c5f8df6c4fd31fe215094ac3024dfc4_2_mwpm_03200403.jpeg","thumbnail_pic_s03":"http://08.imgmini.eastday.com/mobile/20170110/20170110172822_7c5f8df6c4fd31fe215094ac3024dfc4_3_mwpm_03200403.jpeg"}]}
     * error_code : 0
     */

    private String reason;
    private ResultBean result;
    private int error_code;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public static class ResultBean {
        /**
         * stat : 1
         * data : [{"uniquekey":"93c09247175d0e77a71201b10e10856c","title":"真人版《美女与野兽》公仔亮相：女主角丑哭了","date":"2017-01-10 17:28","category":"娱乐","author_name":"环球网","url":"http://mini.eastday.com/mobile/170110172822240.html","thumbnail_pic_s":"http://08.imgmini.eastday.com/mobile/20170110/20170110172822_7c5f8df6c4fd31fe215094ac3024dfc4_1_mwpm_03200403.jpeg","thumbnail_pic_s02":"http://08.imgmini.eastday.com/mobile/20170110/20170110172822_7c5f8df6c4fd31fe215094ac3024dfc4_2_mwpm_03200403.jpeg","thumbnail_pic_s03":"http://08.imgmini.eastday.com/mobile/20170110/20170110172822_7c5f8df6c4fd31fe215094ac3024dfc4_3_mwpm_03200403.jpeg"}]
         */

        private String stat;
        private List<DataBean> data;

        public String getStat() {
            return stat;
        }

        public void setStat(String stat) {
            this.stat = stat;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * uniquekey : 93c09247175d0e77a71201b10e10856c
             * title : 真人版《美女与野兽》公仔亮相：女主角丑哭了
             * date : 2017-01-10 17:28
             * category : 娱乐
             * author_name : 环球网
             * url : http://mini.eastday.com/mobile/170110172822240.html
             * thumbnail_pic_s : http://08.imgmini.eastday.com/mobile/20170110/20170110172822_7c5f8df6c4fd31fe215094ac3024dfc4_1_mwpm_03200403.jpeg
             * thumbnail_pic_s02 : http://08.imgmini.eastday.com/mobile/20170110/20170110172822_7c5f8df6c4fd31fe215094ac3024dfc4_2_mwpm_03200403.jpeg
             * thumbnail_pic_s03 : http://08.imgmini.eastday.com/mobile/20170110/20170110172822_7c5f8df6c4fd31fe215094ac3024dfc4_3_mwpm_03200403.jpeg
             */

            private String uniquekey;
            private String title;
            private String date;
            private String category;
            private String author_name;
            private String url;
            private String thumbnail_pic_s;
            private String thumbnail_pic_s02;
            private String thumbnail_pic_s03;

            public String getUniquekey() {
                return uniquekey;
            }

            public void setUniquekey(String uniquekey) {
                this.uniquekey = uniquekey;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public String getAuthor_name() {
                return author_name;
            }

            public void setAuthor_name(String author_name) {
                this.author_name = author_name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getThumbnail_pic_s() {
                return thumbnail_pic_s;
            }

            public void setThumbnail_pic_s(String thumbnail_pic_s) {
                this.thumbnail_pic_s = thumbnail_pic_s;
            }

            public String getThumbnail_pic_s02() {
                return thumbnail_pic_s02;
            }

            public void setThumbnail_pic_s02(String thumbnail_pic_s02) {
                this.thumbnail_pic_s02 = thumbnail_pic_s02;
            }

            public String getThumbnail_pic_s03() {
                return thumbnail_pic_s03;
            }

            public void setThumbnail_pic_s03(String thumbnail_pic_s03) {
                this.thumbnail_pic_s03 = thumbnail_pic_s03;
            }
        }
    }
}
