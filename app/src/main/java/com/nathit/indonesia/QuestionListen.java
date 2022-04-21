package com.nathit.indonesia;

import com.nathit.indonesia.Model.QuizListenModel;

import java.util.ArrayList;
import java.util.List;

public class QuestionListen {

    private static List<QuizListenModel> listenQuiz() {

        final List<QuizListenModel> quizListenModelList = new ArrayList<>();

        //สร้าง Object ของ QuestionsList คลาสและพาส Questions พร้อมด้วย Options และ answer
        final QuizListenModel question1 = new QuizListenModel("คำถามที่ 1", "คำตอบที่1","คำตอบที่2","คำตอบที่3","คำตอบที่4","คำตอบที่1", "");
        final QuizListenModel question2 = new QuizListenModel("คำถามที่ 2", "คำตอบที่1","คำตอบที่2","คำตอบที่3","คำตอบที่4","คำตอบที่2", "");
        final QuizListenModel question3 = new QuizListenModel("คำถามที่ 3", "คำตอบที่1","คำตอบที่2","คำตอบที่3","คำตอบที่4","คำตอบที่3", "");
        final QuizListenModel question4 = new QuizListenModel("คำถามที่ 4", "คำตอบที่1","คำตอบที่2","คำตอบที่3","คำตอบที่4","คำตอบที่4", "");
        final QuizListenModel question5 = new QuizListenModel("คำถามที่ 5", "คำตอบที่1","คำตอบที่2","คำตอบที่3","คำตอบที่4","คำตอบที่1", "");

        //เพิ่มคำถามทั้งหมดไหที่ List<QuestionsList>
        quizListenModelList.add(question1);
        quizListenModelList.add(question2);
        quizListenModelList.add(question3);
        quizListenModelList.add(question4);
        quizListenModelList.add(question5);

        return quizListenModelList;
    }

    public static List<QuizListenModel> getQuestions(String selectedName) {
        switch (selectedName) {
            case "Java":
                return listenQuiz();
            default:
                return null;
        }
    }
}
