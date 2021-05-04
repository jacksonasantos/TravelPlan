package com.jacksonasantos.travelplan.ui.utility;

import java.util.HashMap;
import java.util.Map;

public class Abreviations {

    public static final Map<String, String> STATE_MAP;
    static {
        STATE_MAP = new HashMap<>();
        STATE_MAP.put("Alabama", "AL");
        STATE_MAP.put("Alaska", "AK");
        STATE_MAP.put("Alberta", "AB");
        STATE_MAP.put("Arizona", "AZ");
        STATE_MAP.put("Arkansas", "AR");
        STATE_MAP.put("British Columbia", "BC");
        STATE_MAP.put("California", "CA");
        STATE_MAP.put("Colorado", "CO");
        STATE_MAP.put("Connecticut", "CT");
        STATE_MAP.put("Delaware", "DE");
        STATE_MAP.put("District Of Columbia", "DC");
        STATE_MAP.put("Florida", "FL");
        STATE_MAP.put("Georgia", "GA");
        STATE_MAP.put("Guam", "GU");
        STATE_MAP.put("Hawaii", "HI");
        STATE_MAP.put("Idaho", "ID");
        STATE_MAP.put("Illinois", "IL");
        STATE_MAP.put("Indiana", "IN");
        STATE_MAP.put("Iowa", "IA");
        STATE_MAP.put("Kansas", "KS");
        STATE_MAP.put("Kentucky", "KY");
        STATE_MAP.put("Louisiana", "LA");
        STATE_MAP.put("Maine", "ME");
        STATE_MAP.put("Manitoba", "MB");
        STATE_MAP.put("Maryland", "MD");
        STATE_MAP.put("Massachusetts", "MA");
        STATE_MAP.put("Michigan", "MI");
        STATE_MAP.put("Minnesota", "MN");
        STATE_MAP.put("Mississippi", "MS");
        STATE_MAP.put("Missouri", "MO");
        STATE_MAP.put("Montana", "MT");
        STATE_MAP.put("Nebraska", "NE");
        STATE_MAP.put("Nevada", "NV");
        STATE_MAP.put("New Brunswick", "NB");
        STATE_MAP.put("New Hampshire", "NH");
        STATE_MAP.put("New Jersey", "NJ");
        STATE_MAP.put("New Mexico", "NM");
        STATE_MAP.put("New York", "NY");
        STATE_MAP.put("Newfoundland", "NF");
        STATE_MAP.put("North Carolina", "NC");
        STATE_MAP.put("North Dakota", "ND");
        STATE_MAP.put("Northwest Territories", "NT");
        STATE_MAP.put("Nova Scotia", "NS");
        STATE_MAP.put("Nunavut", "NU");
        STATE_MAP.put("Ohio", "OH");
        STATE_MAP.put("Oklahoma", "OK");
        STATE_MAP.put("Ontario", "ON");
        STATE_MAP.put("Oregon", "OR");
        STATE_MAP.put("Pennsylvania", "PA");
        STATE_MAP.put("Prince Edward Island", "PE");
        STATE_MAP.put("Puerto Rico", "PR");
        STATE_MAP.put("Quebec", "QC");
        STATE_MAP.put("Rhode Island", "RI");
        STATE_MAP.put("Saskatchewan", "SK");
        STATE_MAP.put("South Carolina", "SC");
        STATE_MAP.put("South Dakota", "SD");
        STATE_MAP.put("Tennessee", "TN");
        STATE_MAP.put("Texas", "TX");
        STATE_MAP.put("Utah", "UT");
        STATE_MAP.put("Vermont", "VT");
        STATE_MAP.put("Virgin Islands", "VI");
        STATE_MAP.put("Virginia", "VA");
        STATE_MAP.put("Washington", "WA");
        STATE_MAP.put("West Virginia", "WV");
        STATE_MAP.put("Wisconsin", "WI");
        STATE_MAP.put("Wyoming", "WY");
        STATE_MAP.put("Yukon Territory", "YT");

        STATE_MAP.put("Acre", "AC");
        STATE_MAP.put("Alagoas", "AL");
        STATE_MAP.put("Amapá", "AP");
        STATE_MAP.put("Amazonas", "AM");
        STATE_MAP.put("Bahia", "BA");
        STATE_MAP.put("Ceará", "CE");
        STATE_MAP.put("Distrito Federal", "DF");
        STATE_MAP.put("Espírito Santo", "ES");
        STATE_MAP.put("Goiás", "GO");
        STATE_MAP.put("Maranhão", "MA");
        STATE_MAP.put("Mato Grosso", "MT");
        STATE_MAP.put("Mato Grosso do Sul", "MS");
        STATE_MAP.put("Minas Gerais", "MG");
        STATE_MAP.put("Pará", "PA");
        STATE_MAP.put("Paraíba", "PB");
        STATE_MAP.put("Paraná", "PR");
        STATE_MAP.put("Pernambuco", "PE");
        STATE_MAP.put("Piauí", "PI");
        STATE_MAP.put("Rio de Janeiro", "RJ");
        STATE_MAP.put("Rio Grande do Norte", "RN");
        STATE_MAP.put("Rio Grande do Sul", "RS");
        STATE_MAP.put("Rondônia", "RO");
        STATE_MAP.put("Roraima", "RR");
        STATE_MAP.put("Santa Catarina", "SC");
        STATE_MAP.put("São Paulo", "SP");
        STATE_MAP.put("Sergipe", "SE");
        STATE_MAP.put("Tocantins", "TO");
    }

    public static String getAbbreviationFromState(String state) {
        if (STATE_MAP.containsKey(state)) {
            return STATE_MAP.get(state);
        }else{
            return state;
        }
    }
}
