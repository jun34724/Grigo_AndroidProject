package com.devidea.grigoapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;

public class CalendarFragment extends Fragment {

    private View view;
    MaterialCalendarView materialCalendarView;
    EventDecorator eventDecorator;

    TextView textView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_calendar, container, false);
        materialCalendarView = (MaterialCalendarView) view.findViewById(R.id.materialCalendarView);

        textView = view.findViewById(R.id.tv_content);

        //        materialCalendarView.selectRange(d1, d2);

        //달력 형태 지정
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setMinimumDate(CalendarDay.from(2021,0,1))
                .setMaximumDate(CalendarDay.from(2025,11,31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        CalendarDay d1, d2;
        d1 = CalendarDay.from(2021, 7, 15);
        d2 = CalendarDay.from(2021, 7, 19);

        materialCalendarView.addDecorators(new SaturdayDecorator(), new SundayDecorator());

        //날짜 선택 이벤트
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                //month 1달 적게 가져옴 (이유는 모르겠음)
                int month = date.getMonth() + 1;
                int day = date.getDay();
                String dday = month + "-" + day;

                textView.setText(dday);
                System.out.println("시간 :" + date);
                System.out.println("시간 :" + dday);
            }
        });

        ArrayList<CalendarDay> calendarDayList = new ArrayList<>();
        calendarDayList.add(d1);
        calendarDayList.add(d2);

        //날짜에 점 표시하는 이벤트
        eventDecorator = new EventDecorator(Color.RED, calendarDayList);
        materialCalendarView.addDecorators(eventDecorator);

        //System.out.println("날짜 : " + calendarayList);

        return  view;
    }

    //일요일 빨간색으로 표시
    public class SundayDecorator implements DayViewDecorator {

        private final Calendar calendar = Calendar.getInstance();
        public SundayDecorator() {
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            //calendar.day_of_week 1 -> 일요일, 2 -> 월요일 ....
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            //일요일
            return weekDay == Calendar.SUNDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.RED));
        }
    }

    //토요일 빨간색으로 표시
    public class SaturdayDecorator implements DayViewDecorator {

        private final Calendar calendar = Calendar.getInstance();

        public SaturdayDecorator() {
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SATURDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.BLUE));
        }
    }

    //점 표시하는 이벤트
    public class EventDecorator implements DayViewDecorator {

        private int color;
        private ArrayList<CalendarDay> dates;
        public EventDecorator(int color, ArrayList<CalendarDay> dates) {
            this.color = color;
            this.dates = new ArrayList<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day){
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(5, color));
        }

    }
}
