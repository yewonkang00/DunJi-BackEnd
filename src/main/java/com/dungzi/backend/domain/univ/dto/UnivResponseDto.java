package com.dungzi.backend.domain.univ.dto;

import com.dungzi.backend.domain.univ.domain.Univ;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UnivResponseDto {

    @Data
    public static class UnivInfo {
        private UUID univId;
//        private Long univId;
        private String univName;
        private String emailDomain;

        private UnivInfo(Univ univ) {
            this.univId = univ.getUnivId();
            this.univName = univ.getUnivName();
            this.emailDomain = univ.getEmailDomain();
        }

        public static List<UnivInfo> toUnivInfoList(List<Univ> univList) {
            List<UnivInfo> univInfoList = new ArrayList<>();
            if(univList.size() != 0) {
                for (Univ univ : univList) {
                    univInfoList.add(new UnivInfo(univ));
                }
            }
            return univInfoList;
        }
    }
}
