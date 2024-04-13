package com.site.GUFPB.domain.user;

import java.util.Map;

public record QuestionarioDTO(String areaEscolhida, Map<String, Integer> scores) {

}
