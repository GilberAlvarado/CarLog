package com.carlog.gilberto.carlog.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.carlog.gilberto.carlog.R;
import com.carlog.gilberto.carlog.activity.aceite;
import com.carlog.gilberto.carlog.activity.filtroAceite;
import com.carlog.gilberto.carlog.activity.revGral;
import com.carlog.gilberto.carlog.tiposClases.tipoLog;
import com.carlog.gilberto.carlog.tiposClases.tipoSettings;

/**
 * Created by Gilberto on 16/05/2015.
 */
public class dbHelper extends SQLiteOpenHelper {

    private static final String db_NAME = "carlog.sqlite";
    private static final int db_SCHEME_VERSION = 98;
    public static final int MAX_TIPOS_REV = 17; // 18 pq cuenta el 0
    private Context contexto;

    public dbHelper(Context context) {
        super(context, db_NAME, null, db_SCHEME_VERSION);
        contexto = context;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(dbCar.CREATE_TABLE);
        sqLiteDatabase.execSQL(dbLogs.CREATE_TABLE);
        sqLiteDatabase.execSQL(dbRevGral.CREATE_TABLE);
        sqLiteDatabase.execSQL(dbMarcas.CREATE_TABLE);
        sqLiteDatabase.execSQL(dbModelos.CREATE_TABLE);
        sqLiteDatabase.execSQL(dbTiposRevision.CREATE_TABLE);
        sqLiteDatabase.execSQL(dbCorrea.CREATE_TABLE);
        sqLiteDatabase.execSQL(dbBombaAgua.CREATE_TABLE);
        sqLiteDatabase.execSQL(dbFiltroGasolina.CREATE_TABLE);
        sqLiteDatabase.execSQL(dbFiltroAire.CREATE_TABLE);
        sqLiteDatabase.execSQL(dbBujias.CREATE_TABLE);
        sqLiteDatabase.execSQL(dbEmbrague.CREATE_TABLE);
        sqLiteDatabase.execSQL(dbAceite.CREATE_TABLE);
        sqLiteDatabase.execSQL(dbFiltroAceite.CREATE_TABLE);
        sqLiteDatabase.execSQL(dbLogin.SQLCreateLogin);
        sqLiteDatabase.execSQL(dbSettings.CREATE_TABLE);


        /// Inicializamos la tabla de modelos
        String[] lista_modelos = {"Modelo", "GRANDE PUNTO", "NUOVA 500", "PUNTO EVO", "33", "75", "90", "145", "146", "147", "155", "156", "159", "164", "166", "1300", "1600", "1750",
                "2000", "2300", "ALFASUD", "ALFETTA", "4C", "ALFA 6", "AR6", "AR8", "ARNA", "BRERA", "GIULIA", "GIULIETTA", "GT", "GTA", "MI.TO", "MONTREAL", "SPIDER", "RZ", "SZ",
                "D3", "ROADSTER", "B3", "B6", "B7", "B8", "B9", "B10", "B11", "B12", "C1", "C2", "C10", "1300", "1600", "A110", "A310", "BERLINETTE", "10", "240",
                "SPARTANA", "HI-TOPIC", "ROCSTA", "BULLDOG", "CYGNET", "DB", "DB7", "DB9", "LAGONDA", "TICKFORD", "V8", "VANQUISH", "VIRAGE", "VOLANTE", "ZAGATO", "AUDI & AUDI L", "COUPÉ", "CABRIOLET", "AUDI QUATTRO", "V8",
                "50", "75", "80", "90", "100", "200", "A1", "A2", "S1", "RS2", "A3", "S3", "RS3", "A4", "S4", "RS4", "A5", "S5", "SQ5", "RS5", "A6", "S6", "RSQ3", "RS6", "RS7", "A7", "S7", "A8", "S8", "R8", "TT", "Q3", "Q5", "Q7",
                "1000", "ALLEGRO", "AMBASSADOR", "APACHE", "MAESTRO", "MAXI", "METRO", "MINI", "MONTEGO", "PRINCESS", "A112", "A111", "PRIMULA", "Y10", "CF", "CHEVANNE", "MIDI", "RASCAL", "CONTINENTAL", "TURBO R", "FREECLIMBER", "SERIE 5", "SERIE 3",
                "SERIE 7", "SERIE 6", "SERIE 8", "SERIE 1", "2 SERIES", "4 SERIES", "M1", "M3", "M4", "M5", "M6", "Z1", "Z3", "Z4", "Z8", "X1", "X3", "X4", "X5", "X6", "I3", "I8", "02", "1500-2000", "1502", "1600", "1600 GT", "1602", "1800", "1802", "3200", "2000", "2002", "2500", "2800", "3000", "3300", "501", "503", "600", "700", "ISETTA","CENTURY", "ELECTRA", "SKYLARK", "ALLANTE", "ATS", "BLS", "CTS", "DEVILLE", "ELDORADO", "ESCALADE", "FLEETWOOD", "SEVILLE", "SRX", "STS", "XLR", "SEVEN", "CORVETTE", "TAHOE", "ALERO", "ASTRO", "AVEO", "BERETTA", "BLAZER", "C1500", "C2500", "C3500", "CAMARO", "CAPRICE", "CAPTIVA", "CAVALIER", "CELTA", "CORSICA", "CRUCE", "EPICA", "EVANDA", "G20",
                "G30", "HHR", "IMPALA", "KALOS", "LACETTI", "LLANOS", "LUMINA", "MALIBU", "MATIZ", "MONTE CARLO", "NUBIRA", "OPTRA", "ORLANDO", "REZZO", "S10", "SILVERADO", "SPARK", "SUBURBAN", "TACUMA", "TRAILBAZER", "TRANS SPORT", "TRAX", "VIVANT", "VOLT", "300C", "300M", "LE BARON", "CIRRUS", "CONCORDE", "CROSSFIRE", "GS", "NEON", "NEW YORKER", "PT CRUISER", "SARATOGA", "SEBRING", "STRATUS", "VIPER", "VISION", "VOYAGER", "GRAND VOYAGER", "YPSILON",
                "2 CV", "ACADIANE", "AMI 6", "AMI 8", "AX", "AXEL", "BERLINGO", "BX", "DS", "C-ELYSÉE", "C-ZERO", "C1", "C2", "C3", "C3 PLURIEL", "C3 PICASSO", "DS3", "C4", "C4 AIRCROSS", "C4 CACTUS", "C4 PICASSO", "DS4", "DS5", "C5", "C6", "C8", "C15", "C25", "C32", "C35", "C CROSSER", "CX", "DISPATCH", "DYANE", "EVASION", "GS", "ID", "H", "JUMPER", "JUMPY", "LN", "MÉHARI", "NEMO", "RELAY", "SAXO", "SM", "SYNERGIE", "VISA", "XANTIA", "XM", "XSARA PICASSO", "XSARA", "ZX",
                "1210", "1300", "1304", "1307", "1309", "1310", "1410", "DOKKER", "DUSTER", "LOGDY", "LOGAN", "SANDERO", "SOLENZA", "ADVENTRA", "ARANOS", "AVEO", "CHAIRMAN", "CIELO", "DAMAS", "ESPERO", "EVANDA", "GENTRA", "KALOS", "KORANDO", "LABO", "LACETTI", "LANOS", "LEGANZA", "LEMANS", "LUBLIN", "MAGNUS", "MATIZ", "MUSSO", "NEXIA", "NUBIRA", "PRINCE", "REXTON", "REZZO", "ROYALE", "SUPER SALOON", "TACUMA", "TICO", "TOSCA", "WINSTORM", "33", "44", "46", "55", "66", "400",
                "55", "850", "ALTIS", "APPLAUSE", "ASCEND", "ATRAI", "BEGO", "CHARADE", "CHARMANT", "COPEN", "CUORE", "DOMINO", "EXTOL", "FEROZA", "GRAN MOVE", "HANDI", "HIJET", "MATERIA", "MIRA", "MOVE", "ROCKY", "SIRION", "SPARCAR", "SPORTRAK", "TAFT", "TERIOS", "TREVIS", "VALERA", "WILDCAT/ROCKY", "YRV", "2.8", "COUPE", "LANDAULETTE", "LIMOUSINE", "XJ", "XJ 40", "AVENGER", "CALIBER", "CARAVAN", "JOURNEY", "NEON", "NITRO", "STRATUS", "VIPER",
                "125", "127", "ATOU", "CARO", "POLONEZ", "TRUCK", "208", "308", "208/308", "328", "348", "360", "MONDENA", "365", "400", "412", "456", "458", "512", "BARCHIETTA", "FIORANO", "SCAGLIETTI", "CALIFORNIA", "DINO", "ENZO", "F355", "F40", "F430", "MARANELLO", "MONDIAL", "TESTAROSSA", "124", "125", "126", "127", "128", "130", "131", "132", "135", "147", "238", "242", "500", "600", "850", "900", "QUBO", "1500", "ALBEA", "ARGENTA", "BARCHETTA", "BRAVA", "BRAVO", "CAMPAGNOLA", "CINQUECENTO", "COUPÉ", "CROMA", "DOBLO", "DUCATO", "DUNA", "ELBA", "FIORINO", "FREEMONT", "PUNTO", "IDEA", "VIPER", "LINEA", "MAREA", "MARENGO", "MULTIPLA", "PALIO", "PANDA", "PREMIO", "REGATTA", "RITMO", "SCUDO", "SEDICI", "SEICENTO",
                "SIENA", "SPIDER", "STILO", "STRADA", "TALENTO", "TEMPRA", "TIPO", "ULYSSE", "UNO", "X1/9", "ACTIVA", "B-MAX", "CAPRI", "C-MAX", "CONSUL", "CORSAIR", "CORTINA", "COUGAR", "COURRIER/COURIER", "ECONOVAN", "ECOSPORT", "ESCORT", "FIESTA", "FOCUS", "FUSION", "GALAXY", "GRANADA", "GT", "KA", "KUGA", "MAVERICK", "MONDEO", "ORION", "P100 PICK-UP", "PUMA", "RANGER", "SCORPIO", "SIERRA", "S-MAX", "STREETKA", "TAUNUS", "CONNECT", "TRANSIT", "ZEPHYR", "ZODIAC", "AEROSTAR", "BRONCO", "EXPLORER", "MUSTANG", "PROBE", "TAURUS", "WINDSTAR", "GALLOPER", "SANTAMO", "ACCORD", "ACTY", "AIRWAVE", "ASCOT", "AVANCIER", "BALLADE", "BEAT", "CAPA", "CITY", "CIVIC", "CONCERTO", "CR-V", "CR-X", "CR-Z", "DOMANI", "ELEMENT", "FR-V", "HR-V", "INSIGHT", "INTEGRA", "JAZZ", "LAGREAT", "LEGEND", "LIFE", "LOGO", "N600", "NSX", "ODYSSEY",
                "PRELUDE", "QUINTET", "S2000", "SHUTTLE", "STREAM", "THATS", "TODAY", "TORNEO", "Z", "ACCENT", "AMICA", "ATOS", "ATOS PRIME", "COUPÉ", "ELANTRA", "EQUUS/CENTENNIAL", "EXCEL", "GALLOPER", "GENESIS", "GETZ", "GRANDEUR AZERA", "H1", "H100", "HIGHWAY VAN", "I10", "I20", "I30", "I40", "IX20", "IX35", "IX55/VERACRUZ", "LANTRA", "MATRIX", "PLAZA", "PONY", "SANTA FÉ", "SANTAMO", "SATELLITE", "SCOUPE", "SONATA", "STAREX", "STELLAR", "TERRACAN", "TIBURON", "TRAJET", "TUCSON", "VELOSTER", "XG", "G", "M", "Q50", "Q60", "EX", "FX", "ELBA", "KORAL", "MINI", "MINI TRE", "ASCENDER", "ASKA", "D-MAX", "GEMINI", "IMPULSE", "MIDI", "PIAZZA", "PICK-UP", "TROOPER", "DAILY", "MASSIF", "E TYPE", "F TYPE", "XJ", "S-TYPE", "XF", "XJR", "XJS", "XJSC", "XK", "X-TYPE", "CHEROKEE", "CJ WRANGLER", "COMMANDER", "COMPASS", "GRAND CHEROKEE", "PATRIOT", "RENEGADE", "WAGONNEER",
                "BESTA", "CARENS", "CARNIVAL", "CEED", "CERATO", "CLARUS", "CREDOS", "ELAN", "JOICE", "K2500", "K2700", "MARGENTIS", "OPIRUS", "OPTIMA", "PICANTO", "PREGIO", "PRIDE", "RETONA", "RIO", "SEDONA", "SEPHIA", "SHUMA", "SORENTO", "SOUL", "SPORTAGE", "VENGA", "110", "1200", "1300", "1500", "1600", "FORMA", "GRANTA", "KALINA", "KALINKA", "NADESCHDA", "NIVA", "NOVA", "PRIORA", "SAGONA", "SAMARA", "TOSCANA", "COUNTACH", "DIABLO", "ESPADA", "GALLARDO", "JALPA", "JARAMA", "LM", "MIURA", "MURCIELAGO", "URRACO", "A 112", "BETA", "DEDRA", "DELTA", "FLAVIA", "FULVIA", "GAMMA", "KAPPA", "LYBRA", "MUSA", "PHEDRA", "PRISMA", "THEMA", "THESIS", "TREVI", "VOYAGER", "YPSILON", "Y10", "ZETA", "DEFENDER", "DISCOVERY", "FREELANDER", "LAND ROVER", "RANGE ROVER", "MAXUS", "200", "400", "CONVOY", "CUB", "GS 250", "NX", "CT200H", "ES", "GS 300", "GS 350", "GS 430", "GS 450", "GS 460", "GX 460", "IS",
                "IS 220", "IS 250", "IS 300", "IS F", "LFA", "LS 460", "LS 600", "LS 400", "LS 430", "LX 470", "LX 570", "RX 300", "RX 350", "RX350/450H", "RX 400", "SC 430", "SHERPA", "AMBRA", "BE UP", "NOVA", "ECLAT", "ELAN", "ELISE", "ELITE", "ESPIRIT", "EUROPA", "EVORA", "EXCEL", "EXIGE", "LTI", "BOLERO-THAR", "CJ", "GOA-SCORPIO", "800", "200-400", "228", "3200 COUPE", "420/430", "BITURBO", "BORA", "GHIBLI", "GRANCABRIO", "GRANSPORT", "GRANTURISMO", "INDY", "KARIF", "KHAMSIN", "KYALAMI", "MC 12", "MERAK", "MEXICO", "QUATTRO PUERTAS", "SHAMAL", "SPIDER", "SPYDER", "MAYBACH", "121", "323", "616", "626", "818", "929", "1000", "1300", "B", "CX-5", "CX-7", "CX-9", "DEMIO", "E", "MAZDA 2", "MAZDA 3", "MAZDA 5", "MAZDA 6", "MAZDA BT50", "MPV", "MX3", "MX5", "MX6", "PREMACY", "RX5", "RX7", "RX-8", "TRIBUTE", "XEDOS", "8", "170", "CARROCERÍA W201", "AMG GT", "CARROCERÍA W107", "CARROCERÍA W123", "CITAN", "CLASSE A", "CLASSE B", "CLASSE C",
                "CLASSE CLC", "CLASSE CLK", "CLASSE CLS", "CLASSE E", "CLASSE G", "CLASSE GL", "CLASSE GLA", "CLASSE GLK", "CLASSE ML", "CLASSE R", "CLASSE S", "CLASSE SL", "CLASSE SLK", "CLASSE V", "CLASSE CLA", "COUPE", "CLASSE CL", "CABRIOLET", "GULLWING", "HECKFLOSSE", "HENSCHEL", "MB", "PAGODE", "PONTON", "PULLMANN", "CARROCERÍA W129", "SL", "CLASSE SLR", "CLASSE SLS", "SPRINTER", "T1", "TRANSPORTER", "VANEO", "VIANO", "VITO", "CARROCERÍA W116", "CARROCERÍA W124", "CARROCERÍA W126", "CARROCERÍA W140", "MAGNETTE", "MG", "MG F", "MG TF", "MG XPOWER", "MG ZR", "MG ZS", "MG ZT", "MG6", "MGB", "MGC", "MIDGET", "MONTEGO", "MINI", "3000 GT", "380", "ASX", "CANTER", "CARISMA", "CELESTE", "COLT", "CORDIA", "DEBONAIR", "ECLIPSE", "FTO", "GALANT", "GALLOPER", "GRANDIS", "I-MIEV", "L200", "L300", "L400", "LANCER", "LANCER F", "MONTERO", "OUTLANDER", "PAJERO", "PROUDIA", "SANTAMO", "SAPPORO", "SHOGUN", "SIGMA", "SPACE GEAR", "SPACE RUNNER", "SPACE STAR", "SPACE WAGON", "STARION", "TREDIA",
                "4/4", "PLUS", "ITAL", "MARINA", "100NX", "200SX", "Z", "ALMERA", "ALMERA TINO", "BLUEBIRD", "CABSTAR", "CEDRIC", "CHERRY", "CUBE", "DATSUN", "NV200", "GT-R", "INTERSTAR", "JUKE", "KING CAB", "KUBISTAR", "LAUREL", "LEAF", "MAXIMA", "MICRA", "MURANO", "NAVARA", "NOTE", "NP300", "NV400", "PATHFINDER", "PATROL", "PUCK-UP", "PIXO", "PRAIRE", "PRIMASTAR", "PRIMERA", "QASHQAI", "SERENA", "SILVIA", "STANZA", "SUNNY", "TERRANO", "TIIDA", "URVAN", "VANETTE", "VIOLET", "X-TRAIL", "ADAM", "ADMIRAL", "AGILA", "AMPERA", "ANTARA", "ARENA", "ASCONA", "ASTRA", "CALIBRA", "CAMPO", "CASCADA", "COMBO", "COMMODORE", "CORSA", "DIPLOMAT", "FRONTERA", "GT", "OPEL GT", "INSIGNIA", "KADETT", "KAPITAN", "MANTA", "MERIVA", "MOKKA", "MONTEREY", "MONZA", "MOVANO", "OLYMPIA", "OMEGA", "REKORD", "SENATOR", "SIGNUM", "SINTRA", "SPEEDSTER", "TIGRA", "VECTRA", "VIVARO", "ZAFIRA", "104", "106", "107", "108", "204", "205", "206", "206+", "207", "207+", "208", "301", "304", "305", "306", "307", "308", "309", "404", "405",
                "406", "407", "504", "505", "508", "604", "605", "607", "806", "807", "1007", "2008", "3008", "4007", "4008", "5008", "BIPPER", "BOXER", "EXPERT", "ION", "J5", "J7", "J9", "PARTNER", "RANCH", "RCZ", "PORTER", "BONNEVILLE", "FIREBIRD", "PHOENIX", "SUNBIRD", "TRANS SPORT", "MACAN", "356", "911", "912", "914", "924", "928", "944", "959", "968", "BOXSTER", "CARRERA GT", "CAYENNE", "CAYMAN", "PANAMERA", "SÉRIE 300", "SÉRIE 400", "AEROBACK", "GEN 2", "IMPIAN", "JUMBUCK", "PERSONNA", "SALOON", "SATRIA", "WIRA", "ALPINE RENAULT", "AMC", "AVANTIME", "CAPTUR", "CLIO", "ESPACE", "ESTAFETTE", "EPRESS", "EXTRA", "FLUENCE", "FUEGO", "KANGOO", "KOLEOS", "LAGUNA", "LATITUDE", "LUTÉCIA", "MASTER", "MASTER PROPULSION", "MÉGANE", "MÉGANE CLASSIC", "MÉGANE SCENIC", "SCENIC II", "MODUS", "R4", "R5", "R6", "R8", "R9", "R10", "R11", "R12", "R14", "R15", "R16", "R17", "R18", "R19", "R20", "R21", "R25", "R30", "RAPID", "RODEO", "SAFRANE", "SPIDER", "SUPER 5", "THALIA", "TRAFIC", "TWINGO", "TWIZY", "VEL SATIS", "WIND", "ZOÉ",
                "CORNICHE", "PHANTOM", "SILVER", "SÉRIE 100", "SÉRIE 200", "25", "SÉRIE 400", "45", "SÉRIE 600", "75", "SÉRIE 800", "1300", "1500", "SD1", "2000", "ACCLAIM", "ALLEGRO", "AMBASSADOR", "CITYROVER", "DOLOMITE", "GT6", "ITAL", "MAESTRO", "MARINA", "MAXI", "METRO", "MINI", "MONTEGO", "PRINCESS", "RV8", "SPITFIRE", "TR4", "TR5", "TR7", "2200", "A60", "9-5", "90", "900", "9000", "9-3", "9-3x", "95", "96", "9-7x", "99", "124", "127", "128", "131", "132", "133", "600", "850", "ALHAMBRA", "ALTEA", "AROSA", "CORDOBA", "EXEO", "FURA", "IBIZA", "INCA", "LEON", "MALAGA", "MARBELLA", "MII", "PANDA", "RITMO", "RONDA", "TERRA", "TOLEDO", "SÉRIE 100-135", "1000", "CITIGO", "FABIA", "FAVORIT", "FELICIA", "FORMAN", "OCTAVIA", "RAPID", "ROOMSTER", "SUPERB", "YETI", "CROSSBLADE", "FORFOUR", "FORTWO", "ROADSTER", "ACTYON", "ACTYON SPORTS", "CHAIRMAN", "FAMILY", "ISTANA", "KORANDO", "KYRON", "MUSSO", "REXTON", "RODIUS", "STAVIC", "XV", "B9 TRIBECA", "E10", "E12", "FORESTER", "JUSTY", "IMPREZA", "LEGAZY", "L SERIE", "LIBERO",
                "MV", "REX", "SVX", "TREZIA", "VANILLE", "VIVIO", "XT", "CELERIO", "ALTO", "BALENO", "CAPPUCCINO", "CARRY", "GRAND VITARA", "IGNIS", "JIMNY", "KIZASHI", "LIANA", "LJ80", "SAMURAI", "SJ 410", "SPLASH", "SWIFT", "SX4", "VITARA", "WAGON R", "X90", "160", "180", "1000", "1100", "1300-1500", "1307", "1510", "AVENGER", "BAGHEERA", "EXPRESS", "HORIZON", "MURENA", "RANCHO", "SAMBA", "SIMCA", "SOLARA", "SUNBEAM", "TAGORA", "SAFARI", "XENON", "MODEL S", "ROADSTER", "PROACE", "1000/PUBLICA", "4-RUNNER", "AURIS", "AVENSIS", "AVENSIS VERSO", "AYGO", "CAMRY", "CARINA", "CELICA", "COROLLA", "COROLLA VERSO", "CORONA", "CRESSIDA", "CROWN", "DYNA", "GT86", "HARRIER", "HIACE", "HILUX", "IQ", "LAND CRUISER", "LITEACE", "MODELE F", "MR2", "PASEO", "PICNIC", "PREVIA", "PRIUS", "RAV4", "STARLET", "SUPRA", "TERCEL", "URBAN CRUISER", "VERSO", "VERSO S", "YARIS", "YARIS VERSO", "1500", "2.5", "2000", "2500", "ACCLAIM", "DOLOMITE", "GT6", "HERALD", "ITALIA", "SPITFIRE", "STAG", "TOLEDO", "TR 2", "TR 3", "TR 4", "TR 5", "TR 6", "TR 7", "TR 8", "VITESSE", "CHIMAERA", "GRIFFITH", "S",
                "AGILA", "ANTARA", "ARENA", "ASTRA", "ASTRAMAX", "BRAVA", "CALIBRA", "CARLTON", "CAVALIER", "CHEVETTE/CHEVANE", "COMBO", "CORSA", "CRESTA", "FIRENZA", "FRONTERA", "INSIGNIA", "MAGNUM", "MERIVA", "MIDI", "MONTEREY", "MOVANO", "NOVA", "OMEGA", "RASCAL", "ROYALE", "SENATOR", "SIGNUM", "SINTRA", "TIGRA", "VECTRA", "VELOX", "VENTORA", "VICEROY", "VICTOR", "VISCOUNT", "VIVA", "VIVARO", "VX", "VX 220", "ZAFIRA", "1500", "166", "ILTIS", "411", "412", "AMAROK", "BORA", "CADDY", "CC", "COCCINELLE/BEETLE", "CORRADO", "CRAFTER", "DERBY", "EOS", "FOX", "GOL", "GOLF", "GOLF CABRIOLET", "JETTA", "K70", "KARMANN", "LT", "LUPO", "NEW BEETLE", "PARA TI", "PASSAT", "PHAETON", "POLO", "NSU RO80", "SANTANA", "SAVEIRO", "SCIROCCO", "SHARAN", "TARO", "TIGUAN", "TOUAREG", "TOURAN", "TRANSPORTER", "UP", "VENTO", "V60", "66", "140", "164", "240", "242", "244", "245", "260", "264", "265", "340", "343", "345", "360", "440", "460", "480", "740", "760", "780", "850", "940", "960", "C30", "C70", "S70", "V70", "S40", "V40", "V50", "S60", "S80", "S90", "V90", "XC 60", "XC 70", "XC 90", "DUETT", "P 121", "P 122", "P 1800", "P 2200", "P 544",
                "1300", "1100", "FLORIDA", "YUGO", "TAVRIA"};

        String[] lista_img_modelos = {"modelo_inicio", "modelo_abarth_grandepunto", "modelo_abarth_nuova500", "modelo_abarth_puntoevo", "modelo_alfaromeo_33", "modelo_alfaromeo_75", "modelo_alfaromeo_90",
                "modelo_alfaromeo_145", "modelo_alfaromeo_146", "modelo_alfaromeo_147", "modelo_alfaromeo_155", "modelo_alfaromeo_156", "modelo_alfaromeo_159", "modelo_alfaromeo_164", "modelo_alfaromeo_166"
                , "modelo_alfaromeo_1300", "modelo_alfaromeo_1600", "modelo_alfaromeo_1750", "modelo_alfaromeo_2000", "modelo_alfaromeo_2300", "modelo_alfaromeo_alfasud", "modelo_alfaromeo_alfetta", "modelo_alfaromeo_4c"
                , "modelo_alfaromeo_alfa6", "modelo_alfaromeo_ar6", "modelo_alfaromeo_ar8", "modelo_alfaromeo_arna", "modelo_alfaromeo_brera", "modelo_alfaromeo_giulia", "modelo_alfaromeo_giulietta", "modelo_alfaromeo_gt"
                , "modelo_alfaromeo_gta", "modelo_alfaromeo_mito", "modelo_alfaromeo_montreal", "modelo_alfaromeo_spider", "modelo_alfaromeo_rz", "modelo_alfaromeo_sz", "modelo_alpina_d3", "modelo_alpina_roadster"
                , "modelo_alpina_b3", "modelo_alpina_b6", "modelo_alpina_b7", "modelo_alpina_b8", "modelo_alpina_b9", "modelo_alpina_b10", "modelo_alpina_b11", "modelo_alpina_b12"
                , "modelo_alpina_c1", "modelo_alpina_c2", "modelo_alpina_d10", "modelo_alpine_1300", "modelo_alpine_1600", "modelo_alpine_a110", "modelo_alpine_a310", "modelo_alpine_berlinette"
                , "modelo_aro_10", "modelo_aro_240", "modelo_aro_spartana", "modelo_asia_hitopic", "modelo_asia_rocsta", "modelo_astonmartin_bulldog", "modelo_astonmartin_cygnet", "modelo_astonmartin_db"
                , "modelo_astonmartin_db7", "modelo_astonmartin_db9", "modelo_astonmartin_lagonda", "modelo_astonmartin_tickford", "modelo_astonmartin_v8", "modelo_astonmartin_vanquish", "modelo_astonmartin_virage", "modelo_astonmartin_volante"
                , "modelo_astonmartin_zagato", "modelo_audi_audi", "modelo_audi_coupe", "modelo_audi_cabriolet", "modelo_audi_quattro", "modelo_audi_v8", "modelo_audi_50", "modelo_audi_75"
                , "modelo_audi_80", "modelo_audi_90", "modelo_audi_100", "modelo_audi_200", "modelo_audi_a1", "modelo_audi_a2", "modelo_audi_s1", "modelo_audi_rs2"
                , "modelo_audi_a3", "modelo_audi_s3", "modelo_audi_rs3", "modelo_audi_a4", "modelo_audi_s4", "modelo_audi_rs4","modelo_audi_a5", "modelo_audi_s5", "modelo_audi_sq5", "modelo_audi_rs5", "modelo_audi_a6", "modelo_audi_s6", "modelo_audi_rsq3", "modelo_audi_rs6", "modelo_audi_rs7", "modelo_audi_a7", "modelo_audi_s7", "modelo_audi_a8", "modelo_audi_s8", "modelo_audi_r8", "modelo_audi_tt", "modelo_audi_q3", "modelo_audi_q5", "modelo_audi_q7"
                , "modelo_austin_1000", "modelo_austin_allegro", "modelo_austin_ambassador", "modelo_austin_apache", "modelo_austin_maestro", "modelo_austin_maxi", "modelo_austin_metro", "modelo_austin_mini", "modelo_austin_montego", "modelo_austin_princess", "modelo_autobianchi_a112", "modelo_autobianchi_a111", "modelo_autobianchi_primula", "modelo_autobianchi_y10", "modelo_bedford_cf", "modelo_bedford_chevanne", "modelo_bedford_midi", "modelo_bedford_rascal"
                , "modelo_bentley_continental", "modelo_bentley_turbor", "modelo_bertone_freeclimber", "modelo_bmw_serie5", "modelo_bmw_serie3", "modelo_bmw_serie7", "modelo_bmw_serie6", "modelo_bmw_serie8", "modelo_bmw_serie1", "modelo_bmw_2series", "modelo_bmw_4series", "modelo_bmw_m1", "modelo_bmw_m3", "modelo_bmw_m4", "modelo_bmw_m5", "modelo_bmw_m6", "modelo_bmw_z1", "modelo_bmw_z3", "modelo_bmw_z4", "modelo_bmw_z8", "modelo_bmw_x1", "modelo_bmw_x3", "modelo_bmw_x4", "modelo_bmw_x5", "modelo_bmw_x6", "modelo_bmw_i3", "modelo_bmw_i8", "modelo_bmw_02", "modelo_bmw_15002000"
                , "modelo_bmw_1502", "modelo_bmw_1600", "modelo_bmw_1600gt", "modelo_bmw_1602", "modelo_bmw_1800", "modelo_bmw_1802", "modelo_bmw_3200", "modelo_bmw_2000", "modelo_bmw_2002", "modelo_bmw_2500", "modelo_bmw_2800", "modelo_bmw_3000", "modelo_bmw_3300", "modelo_bmw_501", "modelo_bmw_503", "modelo_bmw_600", "modelo_bmw_700", "modelo_bmw_isetta", "modelo_buick_century", "modelo_buick_electra", "modelo_buick_skylark", "modelo_cadillac_allante", "modelo_cadillac_ats", "modelo_cadillac_bls", "modelo_cadillac_cts", "modelo_cadillac_deville", "modelo_cadillac_eldorado", "modelo_cadillac_escalade", "modelo_cadillac_fleetwood"
                , "modelo_cadillac_seville", "modelo_cadillac_srx", "modelo_cadillac_sts", "modelo_cadillac_xlr", "modelo_caterham_seven", "modelo_chevrolet_corvette", "modelo_chevrolet_tahoe", "modelo_chevrolet_alero", "modelo_chevrolet_astro", "modelo_chevrolet_aveo", "modelo_chevrolet_beretta", "modelo_chevrolet_blazer", "modelo_chevrolet_c1500", "modelo_chevrolet_c2500", "modelo_chevrolet_c3500", "modelo_chevrolet_camaro", "modelo_chevrolet_caprice", "modelo_chevrolet_captiva", "modelo_chevrolet_cavalier", "modelo_chevrolet_celta", "modelo_chevrolet_corsica", "modelo_chevrolet_cruce", "modelo_chevrolet_epica", "modelo_chevrolet_evanda", "modelo_chevrolet_g20", "modelo_chevrolet_g30", "modelo_chevrolet_hhr", "modelo_chevrolet_impala", "modelo_chevrolet_kalos", "modelo_chevrolet_lacetti"
                , "modelo_chevrolet_llanos", "modelo_chevrolet_lumina", "modelo_chevrolet_malibu", "modelo_chevrolet_matiz", "modelo_chevrolet_montecarlo", "modelo_chevrolet_nubira", "modelo_chevrolet_optra", "modelo_chevrolet_orlando", "modelo_chevrolet_rezzo", "modelo_chevrolet_s10", "modelo_chevrolet_silverado", "modelo_chevrolet_spark", "modelo_chevrolet_suburban", "modelo_chevrolet_tacuma", "modelo_chevrolet_trailbazer", "modelo_chevrolet_transsport", "modelo_chevrolet_trax", "modelo_chevrolet_vivant", "modelo_chevrolet_volt", "modelo_chrysler_300c", "modelo_chrysler_300m", "modelo_chrysler_lebaron", "modelo_chrysler_cirrus", "modelo_chrysler_concorde", "modelo_chrysler_crossfire", "modelo_chrysler_gs", "modelo_chrysler_neon", "modelo_chrysler_newyorker", "modelo_chrysler_ptcruiser", "modelo_chrysler_saratoga", "modelo_chrysler_sebring", "modelo_chrysler_stratus"
                , "modelo_chrysler_viper", "modelo_chrysler_vision", "modelo_chrysler_voyager", "modelo_chrysler_grandvoyager", "modelo_chrysler_ypsilon", "modelo_citroen_2cv", "modelo_citroen_acadiane", "modelo_citroen_ami6", "modelo_citroen_ami8", "modelo_citroen_ax", "modelo_citroen_axel", "modelo_citroen_berlingo", "modelo_citroen_bx", "modelo_citroen_ds", "modelo_citroen_celysee", "modelo_citroen_czero", "modelo_citroen_c1", "modelo_citroen_c2", "modelo_citroen_c3", "modelo_citroen_c3pluriel", "modelo_citroen_c3picasso", "modelo_citroen_ds3", "modelo_citroen_c4", "modelo_citroen_c4aircross", "modelo_citroen_c4cactus", "modelo_citroen_c4picasso", "modelo_citroen_ds4", "modelo_citroen_ds5", "modelo_citroen_c5", "modelo_citroen_c6"
                , "modelo_citroen_c8", "modelo_citroen_c15", "modelo_citroen_c25", "modelo_citroen_c32", "modelo_citroen_c35", "modelo_citroen_ccrosser", "modelo_citroen_cx", "modelo_citroen_dispatch", "modelo_citroen_dyane", "modelo_citroen_evasion", "modelo_citroen_gs", "modelo_citroen_id", "modelo_citroen_h", "modelo_citroen_jumper", "modelo_citroen_jumpy", "modelo_citroen_ln", "modelo_citroen_mehari", "modelo_citroen_nemo", "modelo_citroen_relay", "modelo_citroen_saxo", "modelo_citroen_sm", "modelo_citroen_synergie", "modelo_citroen_visa", "modelo_citroen_xantia", "modelo_citroen_xm", "modelo_citroen_xsarapicasso", "modelo_citroen_xsara", "modelo_citroen_zx", "modelo_dacia_1210", "modelo_dacia_1300", "modelo_dacia_1304", "modelo_dacia_1307", "modelo_dacia_1309"
                , "modelo_dacia_1310", "modelo_dacia_1410", "modelo_dacia_dokker", "modelo_dacia_duster", "modelo_dacia_lodgy", "modelo_dacia_logan", "modelo_dacia_sandero", "modelo_dacia_solenza", "modelo_daewoo_adventra", "modelo_daewoo_aranos", "modelo_daewoo_aveo", "modelo_daewoo_chairman", "modelo_daewoo_cielo", "modelo_daewoo_damas", "modelo_daewoo_espero", "modelo_daewoo_evanda", "modelo_daewoo_gentra", "modelo_daewoo_kalos", "modelo_daewoo_korando", "modelo_daewoo_labo", "modelo_daewoo_lacetti", "modelo_daewoo_lanos", "modelo_daewoo_leganza", "modelo_daewoo_lemans", "modelo_daewoo_lublin", "modelo_daewoo_magnus", "modelo_daewoo_matiz", "modelo_daewoo_musso"
                , "modelo_daewoo_nexia", "modelo_daewoo_nubira", "modelo_daewoo_prince", "modelo_daewoo_rexton", "modelo_daewoo_rezzo", "modelo_daewoo_royale", "modelo_daewoo_supersaloon", "modelo_daewoo_tacuma", "modelo_daewoo_tico", "modelo_daewoo_tosca", "modelo_daewoo_winstorm", "modelo_daf_33", "modelo_daf_44", "modelo_daf_46", "modelo_daf_55", "modelo_daf_66", "modelo_daf_400", "modelo_daihatsu_55", "modelo_daihatsu_850", "modelo_daihatsu_altis", "modelo_daihatsu_applause", "modelo_daihatsu_ascend", "modelo_daihatsu_atrai", "modelo_daihatsu_bego", "modelo_daihatsu_charade", "modelo_daihatsu_charmant", "modelo_daihatsu_copen", "modelo_daihatsu_cuore", "modelo_daihatsu_domino", "modelo_daihatsu_extol", "modelo_daihatsu_feroza", "modelo_daihatsu_granmove", "modelo_daihatsu_handi", "modelo_daihatsu_hijet", "modelo_daihatsu_materia", "modelo_daihatsu_mira", "modelo_daihatsu_move"
                , "modelo_daihatsu_rocky", "modelo_daihatsu_sirion", "modelo_daihatsu_sparcar", "modelo_daihatsu_sportrak", "modelo_daihatsu_taft", "modelo_daihatsu_terios", "modelo_daihatsu_trevis", "modelo_daihatsu_valera", "modelo_daihatsu_wildcatrocky", "modelo_daihatsu_yrv", "modelo_daimler_28", "modelo_daimler_coupe", "modelo_daimler_landaulette", "modelo_daimler_limousine", "modelo_daimler_xj", "modelo_daimler_xj40", "modelo_dodge_avenger", "modelo_dodge_caliber", "modelo_dodge_caravan", "modelo_dodge_journey", "modelo_dodge_neon", "modelo_dodge_nitro", "modelo_dodge_stratus", "modelo_dodge_viper", "modelo_fsopolski_125", "modelo_fsopolski_127", "modelo_fsopolski_atou", "modelo_fsopolski_caro", "modelo_fsopolski_polonez", "modelo_fsopolski_truck", "modelo_ferrari_208", "modelo_ferrari_308", "modelo_ferrari_208308", "modelo_ferrari_328", "modelo_ferrari_348"
                , "modelo_ferrari_360", "modelo_ferrari_mondena", "modelo_ferrari_365", "modelo_ferrari_400", "modelo_ferrari_412", "modelo_ferrari_456", "modelo_ferrari_458", "modelo_ferrari_512", "modelo_ferrari_barchieta", "modelo_ferrari_fiorano", "modelo_ferrari_scaglietti", "modelo_ferrari_california", "modelo_ferrari_dino", "modelo_ferrari_enzo", "modelo_ferrari_f355", "modelo_ferrari_f40", "modelo_ferrari_f430", "modelo_ferrari_maranello", "modelo_ferrari_mondial", "modelo_ferrari_testarossa", "modelo_fiat_124", "modelo_fiat_125", "modelo_fiat_126", "modelo_fiat_127", "modelo_fiat_128", "modelo_fiat_130", "modelo_fiat_131", "modelo_fiat_132", "modelo_fiat_135", "modelo_fiat_147", "modelo_fiat_238", "modelo_fiat_242", "modelo_fiat_500", "modelo_fiat_600", "modelo_fiat_850", "modelo_fiat_900", "modelo_fiat_qubo", "modelo_fiat_1500", "modelo_fiat_albea", "modelo_fiat_argenta"
                , "modelo_fiat_bachetta", "modelo_fiat_brava", "modelo_fiat_bravo", "modelo_fiat_campagnola", "modelo_fiat_cinquecento", "modelo_fiat_coupe", "modelo_fiat_croma", "modelo_fiat_doblo", "modelo_fiat_ducato", "modelo_fiat_duna", "modelo_fiat_elba", "modelo_fiat_fiorino", "modelo_fiat_freemont", "modelo_fiat_punto", "modelo_fiat_idea", "modelo_fiat_viper", "modelo_fiat_linea", "modelo_fiat_marea", "modelo_fiat_marengo", "modelo_fiat_multipla", "modelo_fiat_palio", "modelo_fiat_panda", "modelo_fiat_premio", "modelo_fiat_regata", "modelo_fiat_ritmo", "modelo_fiat_scudo", "modelo_fiat_sedici", "modelo_fiat_seicento", "modelo_fiat_siena", "modelo_fiat_spider", "modelo_fiat_stilo", "modelo_fiat_strada", "modelo_fiat_talento", "modelo_fiat_tempra", "modelo_fiat_tipo", "modelo_fiat_ulysse", "modelo_fiat_uno", "modelo_fiat_x19", "modelo_ford_activa", "modelo_ford_bmax", "modelo_ford_capri"
                , "modelo_ford_cmax", "modelo_ford_consul", "modelo_ford_corsair", "modelo_ford_cortina", "modelo_ford_cougar", "modelo_ford_courrier", "modelo_ford_econovan", "modelo_ford_ecosport", "modelo_ford_escort", "modelo_ford_fiesta", "modelo_ford_focus", "modelo_ford_fusion", "modelo_ford_galaxy", "modelo_ford_granada", "modelo_ford_gt", "modelo_ford_ka", "modelo_ford_kuga", "modelo_ford_maverick", "modelo_ford_mondeo", "modelo_ford_orion", "modelo_ford_p100", "modelo_ford_puma", "modelo_ford_ranger", "modelo_ford_scorpio", "modelo_ford_sierra", "modelo_ford_smax", "modelo_ford_streetka", "modelo_ford_taunus", "modelo_ford_connect", "modelo_ford_transit", "modelo_ford_zephyr", "modelo_ford_zodiac", "modelo_fordusa_aerostar", "modelo_fordusa_bronco", "modelo_fordusa_explorer", "modelo_fordusa_mustang", "modelo_fordusa_probe", "modelo_fordusa_taurus", "modelo_fordusa_windstar"
                , "modelo_galloper_galloper", "modelo_galloper_santamo", "modelo_honda_accord", "modelo_honda_acty", "modelo_honda_airwave", "modelo_honda_ascot", "modelo_honda_avancier", "modelo_honda_ballade", "modelo_honda_beat", "modelo_honda_capa", "modelo_honda_city", "modelo_honda_civic", "modelo_honda_concerto", "modelo_honda_crv", "modelo_honda_crx", "modelo_honda_crz", "modelo_honda_domani", "modelo_honda_element", "modelo_honda_frv", "modelo_honda_hrv", "modelo_honda_insight", "modelo_honda_integra", "modelo_honda_jazz", "modelo_honda_lagreat", "modelo_honda_legend", "modelo_honda_life", "modelo_honda_logo", "modelo_honda_n600", "modelo_honda_nsx", "modelo_honda_odyssey", "modelo_honda_prelude", "modelo_honda_quintet", "modelo_honda_s2000", "modelo_honda_shuttle", "modelo_honda_stream", "modelo_honda_thats", "modelo_honda_today", "modelo_honda_torneo", "modelo_honda_z"
                , "modelo_hyundai_accent", "modelo_hyundai_amica", "modelo_hyundai_atos", "modelo_hyundai_atosprime", "modelo_hyundai_coupe", "modelo_hyundai_elantra", "modelo_hyundai_equuscentennial", "modelo_hyundai_excel", "modelo_hyundai_galloper", "modelo_hyundai_genesis", "modelo_hyundai_getz", "modelo_hyundai_grandeurazera", "modelo_hyundai_h1", "modelo_hyundai_h100", "modelo_hyundai_hightwayvan", "modelo_hyundai_i10", "modelo_hyundai_i20", "modelo_hyundai_i30", "modelo_hyundai_i40", "modelo_hyundai_ix20", "modelo_hyundai_ix35", "modelo_hyundai_ix55veracruz", "modelo_hyundai_lantra", "modelo_hyundai_matrix", "modelo_hyundai_plaza", "modelo_hyundai_pony", "modelo_hyundai_santafe", "modelo_hyundai_santamo", "modelo_hyundai_satellite", "modelo_hyundai_scoupe", "modelo_hyundai_sonata", "modelo_hyundai_starex", "modelo_hyundai_stellar", "modelo_hyundai_terracan", "modelo_hyundai_tiburon", "modelo_hyundai_trajet", "modelo_hyundai_tucson", "modelo_hyundai_veloster", "modelo_hyundai_xg"
                , "modelo_infiniti_g", "modelo_infiniti_m", "modelo_infiniti_q50", "modelo_infiniti_q60", "modelo_infiniti_ex", "modelo_infiniti_fx", "modelo_innocenti_elba", "modelo_innocenti_koral", "modelo_innocenti_mini", "modelo_innocenti_minitre", "modelo_isuzu_ascender", "modelo_isuzu_aska", "modelo_isuzu_dmax", "modelo_isuzu_gemini", "modelo_isuzu_impulse", "modelo_isuzu_midi", "modelo_isuzu_piazza", "modelo_isuzu_pickup", "modelo_isuzu_trooper", "modelo_iveco_daily", "modelo_iveco_massif", "modelo_jaguar_etype", "modelo_jaguar_ftype", "modelo_jaguar_xj", "modelo_jaguar_stype", "modelo_jaguar_xf", "modelo_jaguar_xjr", "modelo_jaguar_xjs", "modelo_jaguar_xjsc", "modelo_jaguar_xk", "modelo_jaguar_xtype", "modelo_jeep_cherokee", "modelo_jeep_cjwrangler", "modelo_jeep_commander", "modelo_jeep_compass", "modelo_jeep_grandcherokee", "modelo_jeep_patriot", "modelo_jeep_renegade", "modelo_jeep_wagonneer", "modelo_kia_besta", "modelo_kia_carens", "modelo_kia_carnival", "modelo_kia_ceed", "modelo_kia_cerato"
                , "modelo_kia_clarus", "modelo_kia_credos", "modelo_kia_elan", "modelo_kia_joice", "modelo_kia_k2500", "modelo_kia_k2700", "modelo_kia_magentis", "modelo_kia_opirus", "modelo_kia_optima", "modelo_kia_picanto", "modelo_kia_pregio", "modelo_kia_pride", "modelo_kia_retona", "modelo_kia_rio", "modelo_kia_sedona", "modelo_kia_sephia", "modelo_kia_shuma", "modelo_kia_sorento", "modelo_kia_soul", "modelo_kia_sportage", "modelo_kia_venga", "modelo_lada_110", "modelo_lada_1200", "modelo_lada_1300", "modelo_lada_1500", "modelo_lada_1600", "modelo_lada_forma", "modelo_lada_granta", "modelo_lada_kalina", "modelo_lada_kalinka", "modelo_lada_nadeschda", "modelo_lada_niva", "modelo_lada_nova", "modelo_lada_priora", "modelo_lada_sagona", "modelo_lada_samara", "modelo_lada_toscana", "modelo_lamborghini_countach", "modelo_lamborghini_diablo", "modelo_lamborghini_espada", "modelo_lamborghini_gallardo", "modelo_lamborghini_jalpa", "modelo_lamborghini_jarama", "modelo_lamborghini_lm", "modelo_lamborghini_miura", "modelo_lamborghini_murcielago", "modelo_lamborghini_urraco"
                , "modelo_lancia_a112", "modelo_lancia_beta", "modelo_lancia_dedra", "modelo_lancia_delta", "modelo_lancia_flavia", "modelo_lancia_fulvia", "modelo_lancia_gamma", "modelo_lancia_kappa", "modelo_lancia_lybra", "modelo_lancia_musa", "modelo_lancia_phedra", "modelo_lancia_prisma", "modelo_lancia_thema", "modelo_lancia_thesis", "modelo_lancia_trevi", "modelo_lancia_voyager", "modelo_lancia_ypsilon", "modelo_lancia_y10", "modelo_lancia_zeta", "modelo_landrover_defender", "modelo_landrover_discovery", "modelo_landrover_freelander", "modelo_landrover_landrover", "modelo_landrover_rangerover", "modelo_ldv_maxus", "modelo_ldv_200", "modelo_ldv_400", "modelo_ldv_convoy", "modelo_ldv_cub", "modelo_lexus_gs250", "modelo_lexus_nx", "modelo_lexus_ct200h", "modelo_lexus_es", "modelo_lexus_gs300", "modelo_lexus_gs350", "modelo_lexus_gs430", "modelo_lexus_gs450", "modelo_lexus_gs460", "modelo_lexus_gx460", "modelo_lexus_is", "modelo_lexus_is220", "modelo_lexus_is250", "modelo_lexus_is300", "modelo_lexus_isf"
                , "modelo_lexus_lfa", "modelo_lexus_ls460", "modelo_lexus_ls600", "modelo_lexus_ls400", "modelo_lexus_ls430", "modelo_lexus_lx470", "modelo_lexus_lx570", "modelo_lexus_rx300", "modelo_lexus_rx350", "modelo_lexus_rx350450h", "modelo_lexus_rx400", "modelo_lexus_sc430", "modelo_leyland_sherpa", "modelo_ligier_ambra", "modelo_ligier_beup", "modelo_ligier_nova", "modelo_lotus_eclat", "modelo_lotus_elan", "modelo_lotus_elise", "modelo_lotus_elite", "modelo_lotus_espirit", "modelo_lotus_europa", "modelo_lotus_evora", "modelo_lotus_excel", "modelo_lotus_exige", "modelo_lti_lti", "modelo_mahindra_bolero", "modelo_mahindra_cj", "modelo_mahindra_goa", "modelo_maruti_800", "modelo_maserati_200400", "modelo_maserati_228", "modelo_maserati_3200coupe", "modelo_maserati_420430", "modelo_maserati_biturbo", "modelo_maserati_bora", "modelo_maserati_ghibli", "modelo_maserati_grancabrio", "modelo_maserati_gransport", "modelo_maserati_granturismo", "modelo_maserati_indy", "modelo_maserati_karif", "modelo_maserati_khamsin", "modelo_maserati_kyalami", "modelo_maserati_mc12", "modelo_maserati_merak", "modelo_maserati_mexico", "modelo_maserati_quattropuertas", "modelo_maserati_shamal", "modelo_maserati_spider", "modelo_maserati_spyder"
                , "modelo_maybach_maybach", "modelo_mazda_121", "modelo_mazda_323", "modelo_mazda_616", "modelo_mazda_626", "modelo_mazda_818", "modelo_mazda_929", "modelo_mazda_1000", "modelo_mazda_1300", "modelo_mazda_b", "modelo_mazda_cx5", "modelo_mazda_cx7", "modelo_mazda_cx9", "modelo_mazda_demio", "modelo_mazda_e", "modelo_mazda_mazda2", "modelo_mazda_mazda3", "modelo_mazda_mazda5", "modelo_mazda_mazda6", "modelo_mazda_mazdabt50", "modelo_mazda_mpv", "modelo_mazda_mx3", "modelo_mazda_mx5", "modelo_mazda_mx6", "modelo_mazda_premacy", "modelo_mazda_rx5", "modelo_mazda_rx7", "modelo_mazda_rx8", "modelo_mazda_tribute", "modelo_mazda_xedos", "modelo_mercedes_8", "modelo_mercedes_170", "modelo_mercedes_carroceriaw201", "modelo_mercedes_amggt", "modelo_mercedes_carroceriaw107", "modelo_mercedes_carroceriaw123", "modelo_mercedes_citan", "modelo_mercedes_clasea", "modelo_mercedes_claseb", "modelo_mercedes_clasec", "modelo_mercedes_claseclc", "modelo_mercedes_claseclk", "modelo_mercedes_clasecls", "modelo_mercedes_clasee", "modelo_mercedes_claseg", "modelo_mercedes_clasegl", "modelo_mercedes_clasegla", "modelo_mercedes_claseglk", "modelo_mercedes_claseml", "modelo_mercedes_claser"
                , "modelo_mercedes_clases", "modelo_mercedes_clasesl", "modelo_mercedes_claseslk", "modelo_mercedes_clasev", "modelo_mercedes_clasecla", "modelo_mercedes_coupe", "modelo_mercedes_clasecl", "modelo_mercedes_cabriolet", "modelo_mercedes_gullwing", "modelo_mercedes_heckflosse", "modelo_mercedes_henschel", "modelo_mercedes_mb", "modelo_mercedes_pagode", "modelo_mercedes_ponton", "modelo_mercedes_pullmann", "modelo_mercedes_carroceriaw129", "modelo_mercedes_sl", "modelo_mercedes_claseslr", "modelo_mercedes_clasesls", "modelo_mercedes_sprinter", "modelo_mercedes_t1", "modelo_mercedes_transporter", "modelo_mercedes_vaneo", "modelo_mercedes_viano", "modelo_mercedes_vito", "modelo_mercedes_carroceriaw116", "modelo_mercedes_carroceriaw124", "modelo_mercedes_carroceriaw126", "modelo_mercedes_carroceriaw140", "modelo_mg_magnette", "modelo_mg_mg", "modelo_mg_mgf", "modelo_mg_mgtf", "modelo_mg_mgxpower", "modelo_mg_mgzr", "modelo_mg_mgzs", "modelo_mg_mgzt", "modelo_mg_mg6", "modelo_mg_mgb", "modelo_mg_mgc", "modelo_mg_midget", "modelo_mg_montego", "modelo_mini_mini", "modelo_mitsubishi_3000gt", "modelo_mitsubishi_380", "modelo_mitsubishi_asx", "modelo_mitsubishi_canter", "modelo_mitsubishi_carisma"
                , "modelo_mitsubishi_celeste", "modelo_mitsubishi_colt", "modelo_mitsubishi_cordia", "modelo_mitsubishi_debonair", "modelo_mitsubishi_eclipse", "modelo_mitsubishi_fto", "modelo_mitsubishi_galant", "modelo_mitsubishi_galloper", "modelo_mitsubishi_grandis", "modelo_mitsubishi_imiev", "modelo_mitsubishi_l200", "modelo_mitsubishi_l300", "modelo_mitsubishi_l400", "modelo_mitsubishi_lancer", "modelo_mitsubishi_lancerf", "modelo_mitsubishi_montero", "modelo_mitsubishi_outlander", "modelo_mitsubishi_pajero", "modelo_mitsubishi_proudia", "modelo_mitsubishi_santamo", "modelo_mitsubishi_sapporo", "modelo_mitsubishi_shogun", "modelo_mitsubishi_sigma", "modelo_mitsubishi_spacegear", "modelo_mitsubishi_spacerunner", "modelo_mitsubishi_spacestar", "modelo_mitsubishi_spacewagon", "modelo_mitsubishi_starion", "modelo_mitsubishi_tredia", "modelo_morgan_44", "modelo_morgan_plus", "modelo_morris_ital", "modelo_morris_marina", "modelo_nissan_100nx", "modelo_nissan_200sx", "modelo_nissan_z", "modelo_nissan_almera", "modelo_nissan_almeratino", "modelo_nissan_bluebird", "modelo_nissan_cabstar", "modelo_nissan_cedric", "modelo_nissan_cherry", "modelo_nissan_cube", "modelo_nissan_datsun", "modelo_nissan_nv200", "modelo_nissan_gtr", "modelo_nissan_interstar", "modelo_nissan_juke"
                , "modelo_nissan_kingcab", "modelo_nissan_kubistar", "modelo_nissan_laurel", "modelo_nissan_leaf", "modelo_nissan_maxima", "modelo_nissan_micra", "modelo_nissan_murano", "modelo_nissan_navara", "modelo_nissan_note", "modelo_nissan_np300", "modelo_nissan_nv400", "modelo_nissan_pathfinder", "modelo_nissan_patrol", "modelo_nissan_pickup", "modelo_nissan_pixo", "modelo_nissan_prairie", "modelo_nissan_primastar", "modelo_nissan_primera", "modelo_nissan_qashqai", "modelo_nissan_serena", "modelo_nissan_silvia", "modelo_nissan_stanza", "modelo_nissan_sunny", "modelo_nissan_terrano", "modelo_nissan_tiida", "modelo_nissan_urvan", "modelo_nissan_vanette", "modelo_nissan_violet", "modelo_nissan_xtrail", "modelo_opel_adam", "modelo_opel_admiral", "modelo_opel_agila", "modelo_opel_ampera", "modelo_opel_antara", "modelo_opel_arena", "modelo_opel_ascona", "modelo_opel_astra", "modelo_opel_calibra", "modelo_opel_campo", "modelo_opel_cascada", "modelo_opel_combo", "modelo_opel_commodore", "modelo_opel_corsa", "modelo_opel_diplomat", "modelo_opel_frontera", "modelo_opel_gt", "modelo_opel_opelgt", "modelo_opel_insignia", "modelo_opel_kadett", "modelo_opel_kapitan", "modelo_opel_manta", "modelo_opel_meriva", "modelo_opel_mokka", "modelo_opel_monterey", "modelo_opel_monza", "modelo_opel_movano", "modelo_opel_olympia", "modelo_opel_omega", "modelo_opel_rekord"
                , "modelo_opel_senator", "modelo_opel_signum", "modelo_opel_sintra", "modelo_opel_speedster", "modelo_opel_tigra", "modelo_opel_vectra", "modelo_opel_vivaro", "modelo_opel_zafira", "modelo_peugeot_104", "modelo_peugeot_106", "modelo_peugeot_107", "modelo_peugeot_108", "modelo_peugeot_204", "modelo_peugeot_205", "modelo_peugeot_206", "modelo_peugeot_206plus", "modelo_peugeot_207", "modelo_peugeot_207plus", "modelo_peugeot_208", "modelo_peugeot_301", "modelo_peugeot_304", "modelo_peugeot_305", "modelo_peugeot_306", "modelo_peugeot_307", "modelo_peugeot_308", "modelo_peugeot_309", "modelo_peugeot_404", "modelo_peugeot_405", "modelo_peugeot_406", "modelo_peugeot_407", "modelo_peugeot_504", "modelo_peugeot_505", "modelo_peugeot_508", "modelo_peugeot_604", "modelo_peugeot_605", "modelo_peugeot_607", "modelo_peugeot_806", "modelo_peugeot_807", "modelo_peugeot_1007", "modelo_peugeot_2008", "modelo_peugeot_3008", "modelo_peugeot_4007", "modelo_peugeot_4008", "modelo_peugeot_5008", "modelo_peugeot_bipper", "modelo_peugeot_boxer", "modelo_peugeot_expert", "modelo_peugeot_ion", "modelo_peugeot_j5", "modelo_peugeot_j7", "modelo_peugeot_j9", "modelo_peugeot_partner", "modelo_peugeot_ranch", "modelo_peugeot_rcz", "modelo_piaggio_porter", "modelo_pontiac_bonneville", "modelo_pontiac_firebird", "modelo_pontiac_phoenix", "modelo_pontiac_sunbird", "modelo_pontiac_transsport"
                , "modelo_porsche_macan", "modelo_porsche_356", "modelo_porsche_911", "modelo_porsche_912", "modelo_porsche_914", "modelo_porsche_924", "modelo_porsche_928", "modelo_porsche_944", "modelo_porsche_959", "modelo_porsche_968", "modelo_porsche_boxster", "modelo_porsche_carreragt", "modelo_porsche_cayenne", "modelo_porsche_cayman", "modelo_porsche_panamera", "modelo_proton_serie300", "modelo_proton_serie400", "modelo_proton_aeroback", "modelo_proton_gen2", "modelo_proton_impian", "modelo_proton_jumbuck", "modelo_proton_personna", "modelo_proton_saloon", "modelo_proton_satria", "modelo_proton_wira", "modelo_renault_alpinerenault", "modelo_renault_amc", "modelo_renault_avantime", "modelo_renault_captur", "modelo_renault_clio", "modelo_renault_espace", "modelo_renault_estafette", "modelo_renault_express", "modelo_renault_extra", "modelo_renault_fluence", "modelo_renault_fuego", "modelo_renault_kangoo", "modelo_renault_koleos", "modelo_renault_laguna", "modelo_renault_latitude", "modelo_renault_lutecia", "modelo_renault_master", "modelo_renault_masterpropulsion", "modelo_renault_megane", "modelo_renault_meganeclassic", "modelo_renault_meganescenic", "modelo_renault_scenicii", "modelo_renault_modus", "modelo_renault_r4", "modelo_renault_r5", "modelo_renault_r6", "modelo_renault_r8", "modelo_renault_r9", "modelo_renault_r10", "modelo_renault_r11"
                , "modelo_renault_r12", "modelo_renault_r14", "modelo_renault_r15", "modelo_renault_r16", "modelo_renault_r17", "modelo_renault_r18", "modelo_renault_r19", "modelo_renault_r20", "modelo_renault_r21", "modelo_renault_r25", "modelo_renault_r30", "modelo_renault_rapid", "modelo_renault_rodeo", "modelo_renault_safrane", "modelo_renault_spider", "modelo_renault_super5", "modelo_renault_thalia", "modelo_renault_trafic", "modelo_renault_twingo", "modelo_renault_twizy", "modelo_renault_velsatis", "modelo_renault_wind", "modelo_renault_zoe", "modelo_rollsroyce_corniche", "modelo_rollsroyce_phantom", "modelo_rollsroyce_silver", "modelo_rover_serie100", "modelo_rover_serie200", "modelo_rover_25", "modelo_rover_serie400", "modelo_rover_45", "modelo_rover_serie600", "modelo_rover_75", "modelo_rover_serie800", "modelo_rover_1300", "modelo_rover_1500", "modelo_rover_sd1", "modelo_rover_2000", "modelo_rover_acclaim", "modelo_rover_allegro", "modelo_rover_ambassador", "modelo_rover_cityrover", "modelo_rover_dolomite", "modelo_rover_gt6", "modelo_rover_ital", "modelo_rover_maestro", "modelo_rover_marina", "modelo_rover_maxi", "modelo_rover_metro", "modelo_rover_mini", "modelo_rover_montego", "modelo_rover_princess", "modelo_rover_rv8", "modelo_rover_spitfire", "modelo_rover_tr4", "modelo_rover_tr5", "modelo_rover_tr7", "modelo_rover_2200", "modelo_rover_a60"
                , "modelo_saab_95", "modelo_saab_90", "modelo_saab_900", "modelo_saab_9000", "modelo_saab_93", "modelo_saab_93x", "modelo_saab_95", "modelo_saab_96", "modelo_saab_97x", "modelo_saab_99", "modelo_seat_124", "modelo_seat_127", "modelo_seat_128", "modelo_seat_131", "modelo_seat_132", "modelo_seat_133", "modelo_seat_600", "modelo_seat_850", "modelo_seat_alhambra", "modelo_seat_altea", "modelo_seat_arosa", "modelo_seat_cordoba", "modelo_seat_exeo", "modelo_seat_fura", "modelo_seat_ibiza", "modelo_seat_inca", "modelo_seat_leon", "modelo_seat_malaga", "modelo_seat_marbella", "modelo_seat_mii", "modelo_seat_panda", "modelo_seat_ritmo", "modelo_seat_ronda", "modelo_seat_terra", "modelo_seat_toledo", "modelo_skoda_serie100135", "modelo_skoda_1000", "modelo_skoda_citigo", "modelo_skoda_fabia", "modelo_skoda_favorit", "modelo_skoda_felicia", "modelo_skoda_forman", "modelo_skoda_octavia", "modelo_skoda_rapid", "modelo_skoda_roomster", "modelo_skoda_superb", "modelo_skoda_yeti", "modelo_smart_crossblade", "modelo_smart_forfour", "modelo_smart_fortwo", "modelo_smart_roadster"
                , "modelo_ssangyong_actyon", "modelo_ssangyong_actyonsports", "modelo_ssangyong_chairman", "modelo_ssangyong_family", "modelo_ssangyong_istana", "modelo_ssangyong_korando", "modelo_ssangyong_kyron", "modelo_ssangyong_musso", "modelo_ssangyong_rexton", "modelo_ssangyong_rodius", "modelo_ssangyong_stavic", "modelo_subaru_xv", "modelo_subaru_b9tribeca", "modelo_subaru_e10", "modelo_subaru_e12", "modelo_subaru_forester", "modelo_subaru_justy", "modelo_subaru_impreza", "modelo_subaru_legazy", "modelo_subaru_lserie", "modelo_subaru_libero", "modelo_subaru_mv", "modelo_subaru_rex", "modelo_subaru_svx", "modelo_subaru_trezia", "modelo_subaru_vanille", "modelo_subaru_vivio", "modelo_subaru_xt", "modelo_suzuki_celerio", "modelo_suzuki_alto", "modelo_suzuki_baleno", "modelo_suzuki_cappuccino", "modelo_suzuki_carry", "modelo_suzuki_grandvitara", "modelo_suzuki_ignis", "modelo_suzuki_jimny", "modelo_suzuki_kizashi", "modelo_suzuki_liana", "modelo_suzuki_lj80", "modelo_suzuki_samurai", "modelo_suzuki_sj410", "modelo_suzuki_splash", "modelo_suzuki_swift", "modelo_suzuki_sx4", "modelo_suzuki_vitara", "modelo_suzuki_wagonr", "modelo_suzuki_x90", "modelo_talbot_160", "modelo_talbot_180", "modelo_talbot_1000", "modelo_talbot_1100", "modelo_talbot_13001500", "modelo_talbot_1307", "modelo_talbot_1510", "modelo_talbot_avenger", "modelo_talbot_bagheera", "modelo_talbot_express", "modelo_talbot_horizon"
                , "modelo_talbot_murena", "modelo_talbot_rancho", "modelo_talbot_samba", "modelo_talbot_simca", "modelo_talbot_solara", "modelo_talbot_sunbeam", "modelo_talbot_tagora", "modelo_tata_safari", "modelo_tata_xenon", "modelo_tesla_models", "modelo_tesla_roadster", "modelo_toyota_proace", "modelo_toyota_1000publica", "modelo_toyota_4runner", "modelo_toyota_auris", "modelo_toyota_avensis", "modelo_toyota_avensisverso", "modelo_toyota_aygo", "modelo_toyota_camry", "modelo_toyota_carina", "modelo_toyota_celica", "modelo_toyota_corolla", "modelo_toyota_corollaverso", "modelo_toyota_corona", "modelo_toyota_cressida", "modelo_toyota_crown", "modelo_toyota_dyna", "modelo_toyota_gt86", "modelo_toyota_harrier", "modelo_toyota_hiace", "modelo_toyota_hilux", "modelo_toyota_iq", "modelo_toyota_landcruiser", "modelo_toyota_liteace", "modelo_toyota_modelef", "modelo_toyota_mr2", "modelo_toyota_paseo", "modelo_toyota_picnic", "modelo_toyota_previa", "modelo_toyota_prius", "modelo_toyota_rav4", "modelo_toyota_starlet", "modelo_toyota_supra", "modelo_toyota_tercel", "modelo_toyota_urbancruiser", "modelo_toyota_verso", "modelo_toyota_versos", "modelo_toyota_yaris", "modelo_toyota_yarisverso", "modelo_triumph_1500", "modelo_triumph_25", "modelo_triumph_2000", "modelo_triumph_2500", "modelo_triumph_acclaim", "modelo_triumph_dolomite", "modelo_triumph_gt6", "modelo_triumph_herald", "modelo_triumph_italia", "modelo_triumph_spitfire"
                , "modelo_triumph_stag", "modelo_triumph_toledo", "modelo_triumph_tr2", "modelo_triumph_tr3", "modelo_triumph_tr4", "modelo_triumph_tr5", "modelo_triumph_tr6", "modelo_triumph_tr7", "modelo_triumph_tr8", "modelo_triumph_vitesse", "modelo_tvr_chimaera", "modelo_tvr_griffith", "modelo_tvr_s", "modelo_vauxhall_agila", "modelo_vauxhall_antara", "modelo_vauxhall_arena", "modelo_vauxhall_astra", "modelo_vauxhall_astramax", "modelo_vauxhall_brava", "modelo_vauxhall_calibra", "modelo_vauxhall_carlton", "modelo_vauxhall_cavalier", "modelo_vauxhall_chevettechevane", "modelo_vauxhall_combo", "modelo_vauxhall_corsa", "modelo_vauxhall_cresta", "modelo_vauxhall_firenza", "modelo_vauxhall_frontera", "modelo_vauxhall_insignia", "modelo_vauxhall_magnum", "modelo_vauxhall_meriva", "modelo_vauxhall_midi", "modelo_vauxhall_monterey", "modelo_vauxhall_movano", "modelo_vauxhall_nova", "modelo_vauxhall_omega", "modelo_vauxhall_rascal", "modelo_vauxhall_royale", "modelo_vauxhall_senator", "modelo_vauxhall_signum", "modelo_vauxhall_sintra", "modelo_vauxhall_tigra", "modelo_vauxhall_vectra", "modelo_vauxhall_velox", "modelo_vauxhall_ventora", "modelo_vauxhall_viceroy", "modelo_vauxhall_victor", "modelo_vauxhall_viscount", "modelo_vauxhall_viva", "modelo_vauxhall_vivaro", "modelo_vauxhall_vx", "modelo_vauxhall_vx220", "modelo_vauxhall_zafira"
                , "modelo_volkswagen_1500", "modelo_volkswagen_166", "modelo_volkswagen_iltis", "modelo_volkswagen_411", "modelo_volkswagen_412", "modelo_volkswagen_amarok", "modelo_volkswagen_bora", "modelo_volkswagen_caddy", "modelo_volkswagen_cc", "modelo_volkswagen_coccinellebettle", "modelo_volkswagen_corrado", "modelo_volkswagen_cafter", "modelo_volkswagen_derby", "modelo_volkswagen_eos", "modelo_volkswagen_fox", "modelo_volkswagen_gol", "modelo_volkswagen_golf", "modelo_volkswagen_golfcabriolet", "modelo_volkswagen_jetta", "modelo_volkswagen_k70", "modelo_volkswagen_karmann", "modelo_volkswagen_lt", "modelo_volkswagen_lupo", "modelo_volkswagen_newbeetle", "modelo_volkswagen_parati", "modelo_volkswagen_passat", "modelo_volkswagen_phaeton", "modelo_volkswagen_polo", "modelo_volkswagen_nsuro80", "modelo_volkswagen_santana", "modelo_volkswagen_saveiro", "modelo_volkswagen_scirocco", "modelo_volkswagen_sharan", "modelo_volkswagen_taro", "modelo_volkswagen_tiguan", "modelo_volkswagen_touareg", "modelo_volkswagen_touran", "modelo_volkswagen_transporter", "modelo_volkswagen_up", "modelo_volkswagen_vento", "modelo_volvo_v60", "modelo_volvo_66", "modelo_volvo_140", "modelo_volvo_164", "modelo_volvo_240", "modelo_volvo_242", "modelo_volvo_244", "modelo_volvo_245", "modelo_volvo_260", "modelo_volvo_264", "modelo_volvo_265", "modelo_volvo_340", "modelo_volvo_343", "modelo_volvo_345", "modelo_volvo_360", "modelo_volvo_440", "modelo_volvo_460", "modelo_volvo_480", "modelo_volvo_740", "modelo_volvo_760"
                , "modelo_volvo_780", "modelo_volvo_850", "modelo_volvo_940", "modelo_volvo_960", "modelo_volvo_c30", "modelo_volvo_c70", "modelo_volvo_s70", "modelo_volvo_v70", "modelo_volvo_s40", "modelo_volvo_v40", "modelo_volvo_v50", "modelo_volvo_s60", "modelo_volvo_s80", "modelo_volvo_s90", "modelo_volvo_v90", "modelo_volvo_xc60", "modelo_volvo_xc70", "modelo_volvo_xc90", "modelo_volvo_duett", "modelo_volvo_p121", "modelo_volvo_p122", "modelo_volvo_p1800", "modelo_volvo_p2200", "modelo_volvo_p544", "modelo_wartburg_1300", "modelo_yugo_1100", "modelo_yugo_florida", "modelo_yugo_yugo", "modelo_zaz_tavria"};

        int[] lista_marca_modelos = {0, 1, 1, 1,2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 5, 5, 6, 6, 7,
                7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8,
                9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 10, 10, 10, 10, 11, 11, 11, 11, 12, 12, 13, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14,
                14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 15, 15, 15, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16,
                17, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18,
                18, 18, 18, 18, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20,
                20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 21, 21, 21,
                21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 23, 23, 23, 23, 23, 23,
                24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 25, 25, 25, 25, 25, 25, 26, 26, 26, 26, 26, 26, 26, 26,
                27, 27, 27, 27, 27, 27, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29,
                29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 30, 30, 30, 30, 30,
                30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 31, 31, 31, 31, 31, 31, 31, 32, 32, 33, 33, 33, 33, 33, 33,
                33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34,
                34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 35, 35, 35, 35, 35, 35, 36, 36, 36, 36, 37, 37, 37, 37, 37, 37, 37, 37, 37, 38, 38, 39, 39, 39,
                39, 39, 39, 39, 39, 39, 39, 40, 40, 40, 40, 40, 40, 40, 40, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 42, 42, 42, 42, 42,
                42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 43, 43, 43, 43, 43, 43, 43, 43, 43, 43, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 45, 45, 45, 45, 45,
                46, 46, 46, 46, 46, 47, 47, 47, 47, 47, 47, 47, 47, 47, 47, 47, 47, 47, 47, 47, 47, 47, 47, 47, 47, 47, 47, 47, 47, 47, 47, 47, 48, 49, 49, 49, 50, 50, 50, 50, 50, 50, 50, 50, 50, 51,
                52, 52, 52, 53, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 55, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56,
                56, 56, 56, 56, 56, 56, 56, 56, 56, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57,
                57, 57, 57, 57, 57, 57, 57, 57, 57, 58, 58, 58, 58, 58, 58, 58, 58, 58, 58, 58, 58, 58, 59, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60,
                60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 61, 61, 62, 62, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63,
                63, 63, 63, 63, 63, 63, 63, 63, 63, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64,
                65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 66,
                67, 67, 67, 67, 67, 68, 68, 68, 68, 68, 68, 68, 68, 68, 68, 68, 68, 68, 68, 68, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70,
                70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 71, 71, 71, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72,
                72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 73, 73, 73, 73, 73, 73, 73, 73, 73, 73, 74, 74, 74, 74, 74, 74, 74, 74, 74, 74,
                74, 74, 74, 74, 74, 74, 74, 74, 74, 74, 74, 74, 74, 74, 74, 75, 75, 75, 75, 75, 75, 75, 75, 75, 75, 75, 75, 76, 76, 76, 76, 77, 77, 77, 77, 77, 77, 77, 77, 77, 77, 77, 78, 78, 78, 78, 78,
                78, 78, 78, 78, 78, 78, 78, 78, 78, 78, 78, 78, 79, 79, 79, 79, 79, 79, 79, 79, 79, 79, 79, 79, 79, 79, 79, 79, 79, 79, 79, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80,
                81, 81, 82, 82, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 84, 84, 84, 84, 84,
                84, 84, 84, 84, 84, 84, 84, 84, 84, 84, 84, 84, 84, 84, 84, 85, 85, 85, 86, 86, 86, 86, 86, 86, 86, 86, 86, 86, 86, 86, 86, 86, 86, 86, 86, 86, 86, 86, 86, 86, 86, 86, 86, 86, 86, 86, 86, 86,
                86, 86, 86, 86, 86, 86, 86, 86, 86, 86, 87, 87, 87, 87, 87, 87, 87, 87, 87, 87, 87, 87, 87, 87, 87, 87, 87, 87, 87, 87, 87, 87, 87, 87, 87, 87, 87, 87, 87, 87, 87, 87, 87, 87, 87, 87, 87, 87, 87, 87,
                88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 89, 90, 90, 90, 91};

        sqLiteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i = 0; i < lista_modelos.length; i++) {
                values.put(dbModelos.CN_ID, i); // el 0 es para el inicio
                values.put(dbModelos.CN_MODELO, lista_modelos[i]);
                values.put(dbModelos.CN_IMG, lista_img_modelos[i]);
                values.put(dbModelos.CN_MARCA, lista_marca_modelos[i]);
                sqLiteDatabase.insert(dbModelos.TABLE_NAME, null, values);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }


        /// Inicializamos la tabla de marcas
        String[] lista_marcas = {"Marca", "Abarth", "Alfa Romeo", "Alpina", "Alpine", "ARO", "Asia", "Aston Martin", "Audi", "Austin", "Autobianchi",
                "Bedford", "Bentley", "Bertone", "BMW", "Buick", "Cadillac", "Caterham", "Chevrolet", "Chrysler", "Citroën", "Dacia", "Daewoo", "DAF",
                "Daihatsu", "Daimler", "Dodge", "F.S.O. / Polski", "Ferrari", "Fiat", "Ford", "Ford USA", "Galloper", "Honda",
                "Hyundai", "Infiniti", "Innocenti", "Isuzu", "Iveco", "Jaguar", "Jeep", "KIA", "Lada", "Lamborghini", "Lancia", "Land Rover", "LDV",
                "Lexus", "Leyland", "Ligier", "Lotus", "LTI", "Mahindra", "Maruti", "Maserati", "Maybach", "Mazda", "Mercedes-Benz", "MG", "Mini",
                "Mitsubishi", "Morgan", "Morris", "Nissan", "Opel", "Peugeot", "Piaggio", "Pontiac", "Porsche", "Proton",
                "Renault", "Rolls-Royce", "Rover", "SAAB", "Seat", "Skoda", "Smart", "Ssangyong", "Subaru", "Suzuki", "Talbot", "TATA",
                "Tesla", "Toyota", "Triumph", "TVR", "Vauxhall", "Volkswagen", "Volvo", "Wartburg", "Yugo/Zastava", "ZAZ TAVRIA"};

        String[] lista_img_marcas = {"logo_inicio", "logo_abarth", "logo_alfaromeo", "logo_alpina", "logo_alpine", "logo_aro", "logo_asia", "logo_astonmartin",
                "logo_audi", "logo_austin", "logo_autobianchi", "logo_bedford", "logo_bentley", "logo_bertone", "logo_bmw", "logo_buick", "logo_cadillac",
                "logo_caterham", "logo_chebrolet", "logo_chrysler", "logo_citroen", "logo_dacia", "logo_daewoo", "logo_daf", "logo_daihatsu", "logo_daimler",
                "logo_dodge", "logo_fso", "logo_ferrari", "logo_fiat", "logo_ford", "logo_fordusa", "logo_galloper", "logo_honda", "logo_hyunday", "logo_infiniti",
                "logo_innocenti", "logo_isuzu", "logo_iveco", "logo_iaguar", "logo_jeep", "logo_kia", "logo_lada", "logo_lamborghini", "logo_lancia",
                "logo_landrover", "logo_ldv", "logo_lexus", "logo_leyland", "logo_ligier", "logo_lotus", "logo_lt1", "logo_mahindra", "logo_maruti", "logo_maserati",
                "logo_maybach", "logo_mazda", "logo_mercedes", "logo_mg", "logo_mini", "logo_mitsubishi", "logo_morgan", "logo_morris", "logo_nissan", "logo_opel",
                "logo_peugeot", "logo_piaggio", "logo_pontiac", "logo_porsche", "logo_proton", "logo_renault", "logo_rollsroyce", "logo_rover", "logo_saab",
                "logo_seat", "logo_skoda", "logo_smart", "logo_ssangyongg", "logo_subaru", "logo_suzuky", "logo_talbot", "logo_tata", "logo_tesla",
                "logo_toyota", "logo_triumph", "logo_tvr", "logo_vauxhall", "logo_volkswagen", "logo_volvo", "logo_wartburg", "logo_yugo", "logo_zaz"};

        sqLiteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i = 0; i < lista_marcas.length; i++) {
                values.put(dbMarcas.CN_ID, i); // el 0 es para el inicio
                values.put(dbMarcas.CN_MARCA, lista_marcas[i]);
                values.put(dbMarcas.CN_IMG, lista_img_marcas[i]);
                sqLiteDatabase.insert(dbMarcas.TABLE_NAME, null, values);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }



        /// Inicializamos la tabla de tipos de revisión
        ////******************SI AÑADIMOS NUEVOS TIPOS O QUITAMOS DEBEMOS ACTUALIZAR LA CONSTANTE MAX_TIPOS_REV  para poder borrar los personalizados*************************//////
        String[] lista_tipo = {contexto.getString(R.string.tipoAceite), contexto.getString(R.string.tipoAmortiguadores), contexto.getString(R.string.tipoAnticongelante), contexto.getString(R.string.tipoBateria), contexto.getString(R.string.tipoBomba), contexto.getString(R.string.tipoBujias), contexto.getString(R.string.tipoCorrea), contexto.getString(R.string.tipoEmbrague),
                contexto.getString(R.string.tipoFiltroAceite), contexto.getString(R.string.tipoFiltroAire),
                contexto.getString(R.string.tipoFiltroGasolina), contexto.getString(R.string.tipoFrenos), contexto.getString(R.string.tipoItv), contexto.getString(R.string.tipoLimpiaparabrisas), contexto.getString(R.string.tipoFrenos),
                contexto.getString(R.string.tipoLuces), contexto.getString(R.string.tipoRevGen), contexto.getString(R.string.tipoRuedas), contexto.getString(R.string.tipoTaller)};
        String[] lista_img = {"ic_aceite", "ic_amortiguadores", "ic_anticongelante", "ic_bateria", "ic_bomba_agua", "ic_bujias", "ic_correa", "ic_embrague", "ic_fil_aceite", "ic_fil_aire", "ic_fil_gasolina", "ic_frenos", "ic_itv_rev",
                "ic_limpiaparabrisas", "ic_liquido_frenos", "ic_luces", "ic_revgen", "ic_ruedas", "ic_taller"};
        ////******************SI AÑADIMOS NUEVOS TIPOS O QUITAMOS DEBEMOS ACTUALIZAR LA CONSTANTE MAX_TIPOS_REV para poder borrar los personalizados *************************//////

        sqLiteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i = 0; i < lista_tipo.length; i++) {
                values.put(dbTiposRevision.CN_TIPO, lista_tipo[i]);
                values.put(dbTiposRevision.CN_IMG, lista_img[i]);
                sqLiteDatabase.insert(dbTiposRevision.TABLE_NAME, null, values);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }



        /// Inicializamos la tabla de tipos de revision general
        String[] lista_revgral = {revGral.TIPO_30K_KM + " " + contexto.getString(R.string.recomendada), revGral.TIPO_5K_KM, revGral.TIPO_10K_KM, revGral.TIPO_15K_KM, revGral.TIPO_20K_KM, revGral.TIPO_25K_KM,
                revGral.TIPO_35K_KM, revGral.TIPO_40K_KM, revGral.TIPO_45K_KM, revGral.TIPO_50K_KM, revGral.TIPO_60K_KM, revGral.TIPO_80K_KM, revGral.TIPO_100K_KM, revGral.TIPO_120K_KM};
        int[] lista_kms =  {30000, 5000, 10000, 15000, 20000, 25000, 35000, 40000, 45000, 50000, 60000, 80000, 100000, 120000};

        sqLiteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i = 0; i < lista_revgral.length; i++) {
                values.put(dbRevGral.CN_ID, i+1);
                values.put(dbRevGral.CN_TIPO, lista_revgral[i]);
                values.put(dbRevGral.CN_KMS, lista_kms[i]);
                sqLiteDatabase.insert(dbRevGral.TABLE_NAME, null, values);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }


        /// Inicializamos la tabla de tipos de correa
        String[] lista_correa = {dbCorrea.TIPO_60K_KM + " " + contexto.getString(R.string.recomendada), dbCorrea.TIPO_80K_KM, dbCorrea.TIPO_100K_KM, dbCorrea.TIPO_120K_KM};
        int[] lista_kms_correa =  {60000, 80000, 100000, 120000};

        sqLiteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i = 0; i < lista_correa.length; i++) {
                values.put(dbCorrea.CN_ID, i+1);
                values.put(dbCorrea.CN_TIPO, lista_correa[i]);
                values.put(dbCorrea.CN_KMS, lista_kms_correa[i]);
                sqLiteDatabase.insert(dbCorrea.TABLE_NAME, null, values);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }

        /// Inicializamos la tabla de tipos de bomba de agua
        String[] lista_bombaagua = {dbBombaAgua.TIPO_80K_KM + " " + contexto.getString(R.string.recomendada), dbBombaAgua.TIPO_60K_KM, dbBombaAgua.TIPO_100K_KM, dbBombaAgua.TIPO_120K_KM};
        int[] lista_kms_bombaagua =  {80000, 60000, 100000, 120000};

        sqLiteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i = 0; i < lista_bombaagua.length; i++) {
                values.put(dbBombaAgua.CN_ID, i+1);
                values.put(dbBombaAgua.CN_TIPO, lista_bombaagua[i]);
                values.put(dbBombaAgua.CN_KMS, lista_kms_bombaagua[i]);
                sqLiteDatabase.insert(dbBombaAgua.TABLE_NAME, null, values);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }


        /// Inicializamos la tabla de tipos de filtro de gasolina
        String[] lista_fgasolina = {dbFiltroGasolina.TIPO_30K_KM + " " + contexto.getString(R.string.recomendada), dbFiltroGasolina.TIPO_40K_KM, dbFiltroGasolina.TIPO_50K_KM, dbFiltroGasolina.TIPO_60K_KM, dbFiltroGasolina.TIPO_80K_KM};
        int[] lista_kms_fgasolina =  {30000, 40000, 500000, 60000, 80000};

        sqLiteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i = 0; i < lista_fgasolina.length; i++) {
                values.put(dbFiltroGasolina.CN_ID, i+1);
                values.put(dbFiltroGasolina.CN_TIPO, lista_fgasolina[i]);
                values.put(dbFiltroGasolina.CN_KMS, lista_kms_fgasolina[i]);
                sqLiteDatabase.insert(dbFiltroGasolina.TABLE_NAME, null, values);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }

        /// Inicializamos la tabla de tipos de filtro de aire
        String[] lista_faire = {dbFiltroAire.TIPO_30K_KM + " " + contexto.getString(R.string.recomendada), dbFiltroAire.TIPO_40K_KM, dbFiltroAire.TIPO_50K_KM, dbFiltroAire.TIPO_60K_KM, dbFiltroAire.TIPO_80K_KM};
        int[] lista_kms_faire =  {30000, 40000, 50000, 60000, 80000};

        sqLiteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i = 0; i < lista_faire.length; i++) {
                values.put(dbFiltroAire.CN_ID, i+1);
                values.put(dbFiltroAire.CN_TIPO, lista_faire[i]);
                values.put(dbFiltroAire.CN_KMS, lista_kms_faire[i]);
                sqLiteDatabase.insert(dbFiltroAire.TABLE_NAME, null, values);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }

        /// Inicializamos la tabla de tipos de bujjias
        String[] lista_bujias = {dbBujias.TIPO_60K_KM + " " + contexto.getString(R.string.recomendada), dbBujias.TIPO_50K_KM, dbBujias.TIPO_55K_KM, dbBujias.TIPO_65K_KM, dbBujias.TIPO_80K_KM};
        int[] lista_kms_bujias =  {60000, 50000, 55000, 65000, 80000};

        sqLiteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i = 0; i < lista_bujias.length; i++) {
                values.put(dbBujias.CN_ID, i+1);
                values.put(dbBujias.CN_TIPO, lista_bujias[i]);
                values.put(dbBujias.CN_KMS, lista_kms_bujias[i]);
                sqLiteDatabase.insert(dbBujias.TABLE_NAME, null, values);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }

        /// Inicializamos la tabla de tipos de filtro de embrague
        String[] lista_embrague = {dbEmbrague.TIPO_60K_KM + " " + contexto.getString(R.string.recomendada), dbEmbrague.TIPO_70K_KM, dbEmbrague.TIPO_80K_KM, dbEmbrague.TIPO_100K_KM, dbEmbrague.TIPO_120K_KM};
        int[] lista_kms_embrague =  {30000, 40000, 500000, 60000, 80000};

        sqLiteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i = 0; i < lista_embrague.length; i++) {
                values.put(dbEmbrague.CN_ID, i+1);
                values.put(dbEmbrague.CN_TIPO, lista_embrague[i]);
                values.put(dbEmbrague.CN_KMS, lista_kms_embrague[i]);
                sqLiteDatabase.insert(dbEmbrague.TABLE_NAME, null, values);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }


        /// Inicializamos la tabla de tipos de aceite
        String[] lista_aceite = {contexto.getString(R.string.minerales), contexto.getString(R.string.semisinteticos), contexto.getString(R.string.sinteticos), contexto.getString(R.string.longlife), contexto.getString(R.string.longlife2)};
        int[] lista_kms2 =  {7500, 10000, 15000, 20000, 30000};

        sqLiteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i = 0; i < lista_aceite.length; i++) {
                values.put(dbAceite.CN_ID, i+1);
                values.put(dbAceite.CN_TIPO, lista_aceite[i]);
                values.put(dbAceite.CN_KMS, lista_kms2[i]);
                sqLiteDatabase.insert(dbAceite.TABLE_NAME, null, values);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }



        /// Inicializamos la tabla de tipos de filtro de aceite
        String[] lista_fil_aceite = {filtroAceite.TIPO_1, filtroAceite.TIPO_2, filtroAceite.TIPO_3};
        int[] lista_veces =  {1, 2, 3};

        sqLiteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i = 0; i < lista_fil_aceite.length; i++) {
                values.put(dbFiltroAceite.CN_ID, i+1);
                values.put(dbFiltroAceite.CN_TIPO, lista_fil_aceite[i]);
                values.put(dbFiltroAceite.CN_VECES, lista_veces[i]);
                sqLiteDatabase.insert(dbFiltroAceite.TABLE_NAME, null, values);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }


        /// Inicializamos la tabla de settings
        sqLiteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i = 0; i < MAX_TIPOS_REV; i++) {
                values.put(dbSettings.CN_ID, i+1);
                values.put(dbSettings.CN_NOTIFICACIONES, tipoSettings.ACTIVO);
                values.put(dbSettings.CN_ACEITE, tipoSettings.ACTIVO);
                values.put(dbSettings.CN_AMORTIGUADORES, tipoSettings.ACTIVO);
                values.put(dbSettings.CN_ANTICONGELANTE, tipoSettings.ACTIVO);
                values.put(dbSettings.CN_BATERIA, tipoSettings.ACTIVO);
                values.put(dbSettings.CN_BOMBAAGUA, tipoSettings.ACTIVO);
                values.put(dbSettings.CN_BUJIAS, tipoSettings.ACTIVO);
                values.put(dbSettings.CN_CORREA, tipoSettings.ACTIVO);
                values.put(dbSettings.CN_FILGASOLINA, tipoSettings.ACTIVO);
                values.put(dbSettings.CN_FILACEITE, tipoSettings.ACTIVO);
                values.put(dbSettings.CN_FILAIRE, tipoSettings.ACTIVO);
                values.put(dbSettings.CN_FRENOS, tipoSettings.ACTIVO);
                values.put(dbSettings.CN_LIQFRENOS, tipoSettings.ACTIVO);
                values.put(dbSettings.CN_LIMPIAPARABRISAS, tipoSettings.ACTIVO);
                values.put(dbSettings.CN_LUCES, tipoSettings.ACTIVO);
                values.put(dbSettings.CN_REVGEN, tipoSettings.ACTIVO);
                values.put(dbSettings.CN_RUEDAS, tipoSettings.ACTIVO);
                values.put(dbSettings.CN_ITV, tipoSettings.ACTIVO);
                values.put(dbSettings.CN_EMBRAGUE, tipoSettings.ACTIVO);
                sqLiteDatabase.insert(dbSettings.TABLE_NAME, null, values);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int old_version, int new_version) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbLogs.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbCar.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbMarcas.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbModelos.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbFiltroAceite.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbTiposRevision.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbAceite.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbCorrea.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbBombaAgua.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbFiltroGasolina.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbFiltroAire.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbBujias.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbEmbrague.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbRevGral.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbLogin.Table);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbSettings.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
