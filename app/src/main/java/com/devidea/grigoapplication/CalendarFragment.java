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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.devidea.grigoapplication.LoginActivity.retrofitService;

public class CalendarFragment extends Fragment {

    private View view;
    MaterialCalendarView materialCalendarView;
    EventDecorator eventDecorator;

    TextView tv_content;
    String s1, content;
    CalendarDay startDay, finishDay;

    ArrayList<ScheduleDTO> scheduleDTOS = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_calendar, container, false);

        materialCalendarView = (MaterialCalendarView) view.findViewById(R.id.materialCalendarView);
        tv_content = view.findViewById(R.id.tv_content);

        //달력 형태 지정
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setMinimumDate(CalendarDay.from(2021,0,1))
                .setMaximumDate(CalendarDay.from(2025,11,31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        materialCalendarView.addDecorators(new SaturdayDecorator(), new SundayDecorator());
        getCalendar();

        //날짜 선택 이벤트
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int month = date.getMonth() + 1;
                int day = date.getDay();
                // 학사일정 date - ex: 날짜 08. 06 으로 받아오는데 getDay하면 6만 가져옴 따라서 10보다 작을 때 앞에 0추가에서 06으로 맞추어줌
                String dday;

                if(day < 10){
                    String day1 = "0" + day;
                    dday = "0" + month + " ." + day1;
                }else{
                    dday = "0" + month + " ." + day;
                }
                for(int i = 0; i < scheduleDTOS.size(); i++){
                    s1 = scheduleDTOS.get(i).getDate();
                    content = scheduleDTOS.get(i).getContent();
                    if(s1.contains(dday)){
                        tv_content.setText(content);
                        break;
                    }else{
                        tv_content.setText("일정이 없습니다.");
                    }
                }
            }
        });

        return  view;
    }

    //일요일 빨간색으로 표시
    public class SundayDecorator implements DayViewDecorator {
        Calendar calendar = Calendar.getInstance();
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

    //토요일 파란색으로 표시
    public class SaturdayDecorator implements DayViewDecorator {
        Calendar calendar = Calendar.getInstance();
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

    //일정을 캘린더에 표시
    public void getCalendar(){
        scheduleDTOS.add(new ScheduleDTO(1, ".08 .18 ~ .08 .18", "[학 부] 2020학년도 후기 학위수여식", 8, 2021));
        scheduleDTOS.add(new ScheduleDTO(2, ".08 .16 ~ .08 .16", "광복절 대체공휴일", 8 , 2021));
        scheduleDTOS.add(new ScheduleDTO(3, ".08 .17 ~ .08 .27", "[학부·대학원] 2학기 등록기간 대체공휴일", 8 , 2021));
        scheduleDTOS.add(new ScheduleDTO(4, ".08 .15 ~ .08 .15", "광복절", 8 , 2021));
        scheduleDTOS.add(new ScheduleDTO(5, ".06 .14 ~ .08 .31", "하계방학", 8 , 2021));
        scheduleDTOS.add(new ScheduleDTO(6, ".08 .17 ~ .08 .17", "[대학원] 2020학년도 후기 학위수여식", 8 , 2021));
        scheduleDTOS.add(new ScheduleDTO(7, ".08 .09 ~ .08 .20", "[학부·대학원] 2학기 수강신청기간", 8 , 2021));
        scheduleDTOS.add(new ScheduleDTO(8, ".08 .02 ~ .08 .06", "하계 집중 휴무기간", 8 , 2021));

//        retrofitService.getSchedule().enqueue(new Callback<ArrayList<ScheduleDTO>>() {
//            @Override
//            public void onResponse(Call<ArrayList<ScheduleDTO>> call, Response<ArrayList<ScheduleDTO>> response) {
//                scheduleDTOS = response.body();
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<ScheduleDTO>> call, Throwable t) {
//
//            }
//        });

        ArrayList<CalendarDay> calendarDayList = new ArrayList<>();
        for(int i = 0; i < scheduleDTOS.size(); i++){
            s1 = scheduleDTOS.get(i).getDate();
            content = scheduleDTOS.get(i).getContent();
//            System.out.println("출력 : " + s1.substring(1,3));
//            System.out.println("출력 : " + s1.substring(5,7));
//            System.out.println("출력 : " + s1.substring(11,13));
//            System.out.println("출력 : " + s1.substring(15,17));
            startDay = CalendarDay.from(scheduleDTOS.get(i).getYear(), Integer.parseInt(s1.substring(1,3)) - 1, Integer.parseInt(s1.substring(5, 7)));
            finishDay = CalendarDay.from(scheduleDTOS.get(i).getYear(), Integer.parseInt(s1.substring(11,13)) - 1, Integer.parseInt(s1.substring(15, 17)));
            calendarDayList.add(startDay);
            calendarDayList.add(finishDay);
        }
        //calendarDayList에 있는 날짜에 점 표시하는 이벤트
        eventDecorator = new EventDecorator(Color.RED, calendarDayList);
        materialCalendarView.addDecorators(eventDecorator);

    }
}
