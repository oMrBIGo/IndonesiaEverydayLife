package com.nathit.indonesia;

import com.nathit.indonesia.Model.QuizListenModel;

import java.util.ArrayList;
import java.util.List;

public class QuestionListen {

    private static List<QuizListenModel> listenQuiz() {

        final List<QuizListenModel> quizListenModelList = new ArrayList<>();

        //สร้าง Object ของ QuestionsList คลาสและพาส Questions พร้อมด้วย Options และ answer


        //เพิ่มคำถามทั้งหมดไหที่ List<QuestionsList>

        return quizListenModelList;
    }

    public static List<QuizListenModel> getQuestions(String selectedName) {
        switch (selectedName) {
            case "แบบฝึกหัดฟังเสียงคำศัพท์":
                return listenQuiz();
            default:
                return null;
        }
    }
}
