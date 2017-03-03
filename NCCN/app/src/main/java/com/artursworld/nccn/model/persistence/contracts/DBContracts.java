package com.artursworld.nccn.model.persistence.contracts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;


public class DBContracts {

    // Useful SQL query parts
    private static final String TEXT_TYPE = " TEXT";
    private static final String DATE_TYPE = " DATE";
    private static final String BLOB_TYPE = " BLOB";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    private DBContracts(){}

    // column name definition
    public static abstract class UserTable {
        public static final String TABLE_NAME = "user";
        public static final String CREATION_DATE= "creation_date";
        public static final String NAME_ID_PK = "name"; // primary key
        public static final String UPDATE_DATE = "update_date";
        public static final String BIRTH_DATE = "birth_date";
        public static final String GENDER = "gender";
        public static final String MARKED_AS_DELETED = "is_deleted";
        public static final String PARTNERSHIP = "partnership";
    }

    public static abstract class HADSDTable {
        public static final String TABLE_NAME = "hadsd";
        public static final String NAME_ID_FK = "user_id"; //foreign key
        public static final String CREATION_DATE_PK = "creation_date";
        public static final String UPDATE_DATE = "update_date";
        public static final String PROGRESS = "progress_of_questionnaire";
        public static final String LAST_QEUSTION_EDITED_NR = "nr_of_last_question_edited";

        // questions
        public static final String ANSWER_TO_QUESTION1 = "ANSWER_TO_QUESTION1";
        public static final String ANSWER_TO_QUESTION2 = "ANSWER_TO_QUESTION2";
        public static final String ANSWER_TO_QUESTION3 = "ANSWER_TO_QUESTION3";
        public static final String ANSWER_TO_QUESTION4 = "ANSWER_TO_QUESTION4";
        public static final String ANSWER_TO_QUESTION5 = "ANSWER_TO_QUESTION5";
        public static final String ANSWER_TO_QUESTION6 = "ANSWER_TO_QUESTION6";
        public static final String ANSWER_TO_QUESTION7 = "ANSWER_TO_QUESTION7";
        public static final String ANSWER_TO_QUESTION8 = "ANSWER_TO_QUESTION8";
        public static final String ANSWER_TO_QUESTION9 = "ANSWER_TO_QUESTION9";
        public static final String ANSWER_TO_QUESTION10 = "ANSWER_TO_QUESTION10";
        public static final String ANSWER_TO_QUESTION11 = "ANSWER_TO_QUESTION11";
        public static final String ANSWER_TO_QUESTION12 = "ANSWER_TO_QUESTION12";
        public static final String ANSWER_TO_QUESTION13 = "ANSWER_TO_QUESTION13";
        public static final String ANSWER_TO_QUESTION14 = "ANSWER_TO_QUESTION14";
    }

    public static abstract class QualityOfLifeTable {
        public static final String TABLE_NAME = "quality_of_life_questionnaire";
        public static final String NAME_ID_FK = "user_id"; //foreign key
        public static final String CREATION_DATE_PK = "creation_date";
        public static final String UPDATE_DATE = "update_date";
        public static final String ANSWERS_TO_QUESTIONS = "answers_to_questions";
        public static final String PROGRESS = "progress_of_questionnaire";
        public static final String LAST_QEUSTION_EDITED_NR = "nr_of_last_question_edited";
    }

    public static abstract class DistressThermometerTable {
        public static final String TABLE_NAME = "distress_thermometer_questionnaire";
        public static final String NAME_ID_FK = "user_id"; //foreign key
        public static final String CREATION_DATE_PK = "creation_date";
        public static final String UPDATE_DATE = "update_date";
        public static final String ANSWERS_TO_QUESTIONS = "answers_to_questions";
        public static final String PROGRESS = "progress_of_questionnaire";
        public static final String LAST_QEUSTION_EDITED_NR = "nr_of_last_question_edited";
    }

    public static abstract class MetaQuestionnaireTable {
        public static final String TABLE_NAME = "metadata_questionnaire";
        public static final String CREATION_DATE_QUESTIONNAIRE = "creation_date"; //primary key
        public static final String UPDATE_DATE = "update_date";
        public static final String OPERATION_TYPE = "operation_type";
    }

    // Create SQL queries
    public static final String CREATE_META_QUESTIONNAIRE_TABLE = "CREATE TABLE "
            + MetaQuestionnaireTable.TABLE_NAME + "("
            + MetaQuestionnaireTable.CREATION_DATE_QUESTIONNAIRE + DATE_TYPE + COMMA_SEP
            + MetaQuestionnaireTable.UPDATE_DATE + DATE_TYPE + COMMA_SEP
            + MetaQuestionnaireTable.OPERATION_TYPE + TEXT_TYPE + COMMA_SEP
            + " PRIMARY KEY ("+MetaQuestionnaireTable.CREATION_DATE_QUESTIONNAIRE +")"
            + ");";

    public static final String CREATE_USER_TABLE = "CREATE TABLE "
            + UserTable.TABLE_NAME + "("
            + UserTable.CREATION_DATE + DATE_TYPE + COMMA_SEP
            + UserTable.NAME_ID_PK + TEXT_TYPE + COMMA_SEP
            + " PRIMARY KEY ("+UserTable.NAME_ID_PK +")"
            + ");";

    public static final String CREATE_HADSD_TABLE = "CREATE TABLE "
            + HADSDTable.TABLE_NAME + "("
            + HADSDTable.CREATION_DATE_PK + DATE_TYPE + COMMA_SEP
            + HADSDTable.NAME_ID_FK + DATE_TYPE + COMMA_SEP
            + HADSDTable.UPDATE_DATE + DATE_TYPE + COMMA_SEP
            + HADSDTable.ANSWER_TO_QUESTION1 + BLOB_TYPE + COMMA_SEP
            + HADSDTable.ANSWER_TO_QUESTION2 + BLOB_TYPE + COMMA_SEP
            + HADSDTable.ANSWER_TO_QUESTION3 + BLOB_TYPE + COMMA_SEP
            + HADSDTable.ANSWER_TO_QUESTION4 + BLOB_TYPE + COMMA_SEP
            + HADSDTable.ANSWER_TO_QUESTION5 + BLOB_TYPE + COMMA_SEP
            + HADSDTable.ANSWER_TO_QUESTION6 + BLOB_TYPE + COMMA_SEP
            + HADSDTable.ANSWER_TO_QUESTION7 + BLOB_TYPE + COMMA_SEP
            + HADSDTable.ANSWER_TO_QUESTION8 + BLOB_TYPE + COMMA_SEP
            + HADSDTable.ANSWER_TO_QUESTION9 + BLOB_TYPE + COMMA_SEP
            + HADSDTable.ANSWER_TO_QUESTION10 + BLOB_TYPE + COMMA_SEP
            + HADSDTable.ANSWER_TO_QUESTION11 + BLOB_TYPE + COMMA_SEP
            + HADSDTable.ANSWER_TO_QUESTION12 + BLOB_TYPE + COMMA_SEP
            + HADSDTable.ANSWER_TO_QUESTION13 + BLOB_TYPE + COMMA_SEP
            + HADSDTable.ANSWER_TO_QUESTION14 + BLOB_TYPE + COMMA_SEP
            + HADSDTable.LAST_QEUSTION_EDITED_NR + INTEGER_TYPE + COMMA_SEP
            + HADSDTable.PROGRESS + INTEGER_TYPE + COMMA_SEP
            + " PRIMARY KEY ("+HADSDTable.CREATION_DATE_PK +") "
            + "FOREIGN KEY(" + HADSDTable.NAME_ID_FK +") "
            + "REFERENCES " + UserTable.TABLE_NAME + "(" + UserTable.NAME_ID_PK +") ON UPDATE CASCADE);";

    public static final String CREATE_QUALITY_OF_TABLE = "CREATE TABLE "
            + QualityOfLifeTable.TABLE_NAME + "("
            + QualityOfLifeTable.CREATION_DATE_PK + DATE_TYPE + COMMA_SEP
            + QualityOfLifeTable.NAME_ID_FK + DATE_TYPE + COMMA_SEP
            + QualityOfLifeTable.UPDATE_DATE + DATE_TYPE + COMMA_SEP
            + QualityOfLifeTable.ANSWERS_TO_QUESTIONS + BLOB_TYPE + COMMA_SEP
            + QualityOfLifeTable.LAST_QEUSTION_EDITED_NR + INTEGER_TYPE + COMMA_SEP
            + QualityOfLifeTable.PROGRESS + INTEGER_TYPE + COMMA_SEP
            + " PRIMARY KEY ("+QualityOfLifeTable.CREATION_DATE_PK +") "
            + "FOREIGN KEY(" + QualityOfLifeTable.NAME_ID_FK +") "
            + "REFERENCES " + UserTable.TABLE_NAME + "(" + UserTable.NAME_ID_PK +") ON UPDATE CASCADE);";

    public static final String CREATE_DISTRESS_THERMOMETER_TABLE = "CREATE TABLE "
            + DistressThermometerTable.TABLE_NAME + "("
            + DistressThermometerTable.CREATION_DATE_PK + DATE_TYPE + COMMA_SEP
            + DistressThermometerTable.NAME_ID_FK + DATE_TYPE + COMMA_SEP
            + DistressThermometerTable.UPDATE_DATE + DATE_TYPE + COMMA_SEP
            + DistressThermometerTable.ANSWERS_TO_QUESTIONS + BLOB_TYPE + COMMA_SEP
            + DistressThermometerTable.LAST_QEUSTION_EDITED_NR + INTEGER_TYPE + COMMA_SEP
            + DistressThermometerTable.PROGRESS + INTEGER_TYPE + COMMA_SEP
            + " PRIMARY KEY ("+DistressThermometerTable.CREATION_DATE_PK +") "
            + "FOREIGN KEY(" + DistressThermometerTable.NAME_ID_FK +") "
            + "REFERENCES " + UserTable.TABLE_NAME + "(" + UserTable.NAME_ID_PK +") ON UPDATE CASCADE);";

    // Helper class manages database creation and version management
    public static class DatabaseHelper extends SQLiteOpenHelper {

        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "app.db";
        private static DatabaseHelper instance;

        public static synchronized DatabaseHelper getHelper(Context context) {
            if (instance == null)
                instance = new DatabaseHelper(context);
            return instance;
        }

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_USER_TABLE);
            db.execSQL(CREATE_HADSD_TABLE);
            db.execSQL(CREATE_QUALITY_OF_TABLE);
            db.execSQL(CREATE_DISTRESS_THERMOMETER_TABLE);
            db.execSQL(CREATE_META_QUESTIONNAIRE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Whenever you design a newer version, make sure to add some migration here.
        }
    }

}
