package dev.steve.blogrestapi.utility;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResponse<T> {

  private int currentPage;
  private int recordInPage;
  private Long totalRecords;
  private int totalPages;
  private Boolean isFirst;
  private Boolean isLast;
  private Boolean hasNext;
  private Boolean hasPrevious;
  private T data;
}
