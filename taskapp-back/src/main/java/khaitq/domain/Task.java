package khaitq.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private Long id;
    private String title;
    private String des;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;
    private Long userId;
}

/*

{
"id":1,
"type": "buy",
"userId": 23,
"symbol": "ABX",
"shares": 30,
"price": 134,
"timestamp": 1531522701000
}

 */
