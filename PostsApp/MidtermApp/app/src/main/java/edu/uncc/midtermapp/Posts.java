package edu.uncc.midtermapp;

import java.util.List;

public class Posts {
    public static class Post{
        public String created_by_name;
        public String post_id;
        public String created_by_uid;

        @Override
        public String toString() {
            return "Post{" +
                    "created_by_name='" + created_by_name + '\'' +
                    ", post_id='" + post_id + '\'' +
                    ", created_by_uid='" + created_by_uid + '\'' +
                    ", post_text='" + post_text + '\'' +
                    ", created_at='" + created_at + '\'' +
                    '}';
        }

        public String post_text;

        public String getCreated_by_name() {
            return created_by_name;
        }

        public void setCreated_by_name(String created_by_name) {
            this.created_by_name = created_by_name;
        }

        public String getPost_id() {
            return post_id;
        }

        public void setPost_id(String post_id) {
            this.post_id = post_id;
        }

        public String getCreated_by_uid() {
            return created_by_uid;
        }

        public void setCreated_by_uid(String created_by_uid) {
            this.created_by_uid = created_by_uid;
        }

        public String getPost_text() {
            return post_text;
        }

        public void setPost_text(String post_text) {
            this.post_text = post_text;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String created_at;
    }

    public String status;
    public List<Post> posts;
    public String page;

    @Override
    public String toString() {
        return "Posts{" +
                "status='" + status + '\'' +
                ", posts=" + posts +
                ", page='" + page + '\'' +
                ", pageSize=" + pageSize +
                ", totalCount=" + totalCount +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int pageSize;
    public int totalCount;
}
