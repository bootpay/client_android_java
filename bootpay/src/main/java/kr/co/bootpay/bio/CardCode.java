package kr.co.bootpay.bio;

import kr.co.bootpay.R;

public class CardCode {
    public static final String BC = "01"; //BC카드
    public static final String  KB = "02"; //국민카드
    public static final String  HN = "03"; //하나카드
    public static final String  SS = "04"; //삼성카드
    public static final String  SH = "06"; //신한카드
    public static final String  HD = "07"; //현대카드
    public static final String  LT = "08"; //롯데카드
    public static final String  CT = "11"; //씨티카드
    public static final String  NH = "12"; //농협카드
    public static final String  SH2 = "13"; //수협카드
    public static final String  SH3 = "14"; //신협카드
    public static final String  GJ = "21"; //광주카드
    public static final String  JB = "22"; //전북카드
    public static final String  JJ = "23"; //제주카드
    public static final String  SHCPT = "24"; //신한캐피탈카드
    public static final String  GVS = "25"; //해외비자
    public static final String  GMST = "26"; //해외마스터

    public static final String  GDNS = "27"; //해외디아너스카드
    public static final String  GAMX = "28"; //해외AMX
    public static final String  GJCB = "29"; //해외JCB
    public static final String  SKOK = "31"; //SK OK Cashbag
    public static final String  POST = "32"; //우체국

    public static final String  SM = "33"; //새마을체크카드
    public static final String  CH = "34"; //중국은행 체크카드
    public static final String  KDB = "35"; //KDB체크카드
    public static final String  HD2 = "36"; //현대증권 체크카드
    public static final String  JC = "37"; //저축은행

    //두번째가 글자
    public static int getColorText(String code) {
        switch (code) {
            case BC:
            case SH:
            case HD:
                return R.color.card_bc;
            case KB:
            case HN:
            case SS:
                return R.color.white;
            case LT:
                return R.color.white;
            case CT:
                return R.color.card_ct;
            case NH:
                return R.color.white;
            case SH2:
                return R.color.white;
            case SH3:
                return R.color.white;
            case GJ:
                return R.color.white;
//            case JB:
//                return R.color.card_jb;
            case JB:
            case JJ:
            case SHCPT:
                return R.color.card_bc;
//                return R.color.card_jj;
//            case GVS:
//                return R.color.card_gvs;
            case GVS:
            case GMST:
            case GDNS:
                return R.color.card_bc;
            case GAMX:
            case GJCB:
                return R.color.white;
            case SKOK:
                return R.color.card_bc;
            case POST:
                return R.color.white;
            case SM:
                return R.color.card_sm;
            case CH:
                return R.color.card_ch;
            case KDB:
                return R.color.card_kdb;
            case HD2:
                return R.color.card_hd2;
            case JC:
                return R.color.white;
            default:
                return R.color.card_default;
        }

    }

    //첫번째가 배경
    public static int getColorBackground(String code) {
        switch (code) {
            case BC:
            case SH:
            case HD:
                return R.drawable.card_bc;
            case KB:
                return R.drawable.card_kb;
            case HN:
                return R.drawable.card_hn;
            case SS:
                return R.drawable.card_ss;
            case LT:
                return R.drawable.card_lt;
            case CT:
            case SH2:
            case SH3:
                return R.drawable.card_sh2;
            case GJ:
                return R.drawable.card_gj;
            case JB:
            case JJ:
            case SHCPT:
                return R.color.card_bc;
            case GVS:
            case GMST:
            case GDNS:
                return R.color.card_bc;
            case GAMX:
                return R.drawable.card_gamx;
            case GJCB:
                return R.drawable.card_gjcb;
            case SKOK:
                return R.color.card_bc;
            case POST:
                return R.drawable.card_post;
            case SM:
                return R.drawable.card_sm;
            case CH:
                return R.drawable.card_ch;
            case KDB:
                return R.drawable.card_kdb;
            case HD2:
                return R.drawable.card_hd2;
            case JC:
                return R.drawable.card_jc;
//            case GMST:
//            case GDNS:
//                return R.drawable.card_default;
//            case GAMX:
//                return R.drawable.



            default:
                return R.drawable.card_default;
        }

    }
}
