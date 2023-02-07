package com.dungzi.backend.domain.univ.api;

import com.dungzi.backend.domain.univ.application.UnivService;
import com.dungzi.backend.domain.univ.dto.UnivRequestDto;
import com.dungzi.backend.domain.univ.dto.UnivResponseDto;
import com.dungzi.backend.global.common.CommonCode;
import com.dungzi.backend.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/univs")
public class UnivController {
    private final UnivService univService;

    @GetMapping("/all")
    public CommonResponse getUnivAll() {
        log.info("[API] univs/all");
        return CommonResponse.toResponse(CommonCode.OK,
                UnivResponseDto.UnivInfo.toUnivInfoList(univService.getUnivAll()));
    }


    // TODO : 임시로 만들고 현재 사용하지 않는 API. 추후 관리자 페이지에 대학교 추가 기능이 필요하면 사용
    @PostMapping("")
    public CommonResponse saveUniv(@RequestBody UnivRequestDto.UnivInfo requestDto) {
        return CommonResponse.toResponse(CommonCode.OK, univService.saveUniv(requestDto.toEntity()));
    }
}
