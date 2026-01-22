package vn.hoidanit.jobhunter.domain.response;

public class ResultPaginationDTO {
    private Meta meta;
    private Object result;
    
    //inner class với object Meta
    public static class Meta{
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

    //getter setter của ResultPaginationDTO
    public Meta getMeta() {
        return meta;
    }

    public Object getResult() {
        return result;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public void setResult(Object result) {
        this.result = result;
    }
    
    
}
