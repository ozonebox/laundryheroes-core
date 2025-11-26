package com.laundryheroes.core.info;


import java.util.List;

public class SupportInfoResponse {

    private String whatsappNumber;
    private String phoneNumber;

    private List<FaqItem> faqs;
    private LegalContent terms;
    private LegalContent privacy;

    public SupportInfoResponse(
            String whatsappNumber,
            String phoneNumber,
            List<FaqItem> faqs,
            LegalContent terms,
            LegalContent privacy
    ) {
        this.whatsappNumber = whatsappNumber;
        this.phoneNumber = phoneNumber;
        this.faqs = faqs;
        this.terms = terms;
        this.privacy = privacy;
    }

    public String getWhatsappNumber() { return whatsappNumber; }
    public String getPhoneNumber() { return phoneNumber; }
    public List<FaqItem> getFaqs() { return faqs; }
    public LegalContent getTerms() { return terms; }
    public LegalContent getPrivacy() { return privacy; }
}
