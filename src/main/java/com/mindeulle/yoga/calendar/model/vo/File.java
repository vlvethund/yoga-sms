package com.mindeulle.yoga.calendar.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class File {
    //
    private String name; // Optional: MMS에서만 사용 가능 공백 사용 불가. jpg, jpeg 확장자를 가진 파일 이름. 최대 40자
    private String body; // Optional: MMS에서만 사용 가능. 공백 사용 불가. jpg, jpeg 이미지를 Base64로 인코딩한 값. 원 파일 기준 최대 300Kbyte. 파일 명 최대 40자. 해상도 최대 1500 * 1500
}
