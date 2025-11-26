package com.laundryheroes.core.info;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupportMetaResponse {
    private SupportInfoResponse support;
    private List<FaqItem> faq;
    private LegalContent terms;
    private LegalContent privacy;
}

