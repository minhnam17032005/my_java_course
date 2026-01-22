package vn.hoidanit.jobhunter.domain.dto;

public class Meta {
    private int page;    //trang hiện tại
    private int pageSize;   //số record trong 1 trang
    private int pages;   //số trang hiện tại
    private long total;     //tổng số record

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPages() {
        return pages;
    }

    public long getTotal() {
        return total;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public void setTotal(long total) {
        this.total = total;
    }


}
