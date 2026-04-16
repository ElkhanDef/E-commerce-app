package com.app.model.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableRequest {

    @Min(value = 0, message = "Sayfa numarası 0'dan küçük olamaz")
    private int pageNumber = 0;

    @Min(value = 1, message = "Sayfa boyutu minimum 1 olmalıdır")
    @Max(value = 100, message = "Sayfa boyutu maksimum 100 olabilir")
    private int pageSize = 10;

    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Sütun adı yalnızca harf, rakam ve alt çizgi içerebilir")
    private String column = "id";

    private boolean asc = false;

    public Pageable toPageable() {
        Sort sort = asc ? Sort.by(column).ascending() : Sort.by(column).descending();
        return PageRequest.of(pageNumber, pageSize, sort);
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }
}
