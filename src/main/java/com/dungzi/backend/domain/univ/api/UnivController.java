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
        return CommonResponse.toResponse(CommonCode.OK,
                UnivResponseDto.UnivInfo.toUnivInfoList(univService.getUnivAll()));
    }

    @PostMapping("")
    public CommonResponse saveUniv(@RequestBody UnivRequestDto.UnivInfo body) {
        return CommonResponse.toResponse(CommonCode.OK, univService.saveUniv(body.toEntity()));
    }
}
