package com.devidea.grigoapplication;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.devidea.grigoapplication.LoginActivity.retrofitService;

public class CalendarFragment extends Fragment {

    private View view;
    private MaterialCalendarView materialCalendarView;
    private EventDecorator eventDecorator;

    private TextView tv_content;
    private String s1, content;
    private CalendarDay startDay, finishDay;

    private ArrayList<ScheduleDTO> scheduleDTOS = new ArrayList<>();
    private CalendarService calendarService = new CalendarService();
    private RetrofitService retrofitCalenderService = calendarService.CreateService();

    Calendar startTimeCalendar = Calendar.getInstance();
    Calendar endTimeCalendar = Calendar.getInstance();

    int currentYear = startTimeCalendar.get(Calendar.YEAR);
    int currentMonth = startTimeCalendar.get(Calendar.MONTH);
    int currentDate = startTimeCalendar.get(Calendar.DATE);



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_calendar, container, false);

        materialCalendarView = (MaterialCalendarView) view.findViewById(R.id.materialCalendarView);
        tv_content = view.findViewById(R.id.tv_content);

        //달력 형태 지정
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setMinimumDate(CalendarDay.from(currentYear, currentMonth, 1))
                .setMaximumDate(CalendarDay.from(currentYear, currentMonth, endTimeCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)))
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

                if (day < 10) {
                    String day1 = "0" + day;
                    dday = "0" + month + " ." + day1;
                } else {
                    dday = "0" + month + " ." + day;
                }
                for (int i = 0; i < scheduleDTOS.size(); i++) {
                    s1 = scheduleDTOS.get(i).getDate();
                    content = scheduleDTOS.get(i).getContent();
                    if (s1.contains(dday)) {
                        tv_content.setText(content);
                        break;
                    } else {
                        tv_content.setText("일정이 없습니다.");
                    }
                }
            }
        });

        return view;
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
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(5, color));
        }

    }

    //일정을 캘린더에 표시
    public void getCalendar() {

        retrofitCalenderService.getSchedule().enqueue(new Callback<ArrayList<ScheduleDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<ScheduleDTO>> call, Response<ArrayList<ScheduleDTO>> response) {
                scheduleDTOS = response.body();
                if(response.body()!=null){
                    calUpdate();
                }

                Log.d("res", scheduleDTOS.get(0).getContent());
                Log.d("res", scheduleDTOS.get(0).getDate());

            }

            @Override
            public void onFailure(Call<ArrayList<ScheduleDTO>> call, Throwable t) {

            }
        });



    }

    private void calUpdate(){
        ArrayList<CalendarDay> calendarDayList = new ArrayList<>();
        for (int i = 0; i < scheduleDTOS.size(); i++) {
            s1 = scheduleDTOS.get(i).getDate();
            content = scheduleDTOS.get(i).getContent();

            startDay = CalendarDay.from(scheduleDTOS.get(i).getYear(), Integer.parseInt(s1.substring(0, 2)) - 1, Integer.parseInt(s1.substring(4, 6)));
            finishDay = CalendarDay.from(scheduleDTOS.get(i).getYear(), Integer.parseInt(s1.substring(10, 12)) - 1, Integer.parseInt(s1.substring(14, 16)));
            calendarDayList.add(startDay);
            calendarDayList.add(finishDay);
        }
        //calendarDayList에 있는 날짜에 점 표시하는 이벤트
        eventDecorator = new EventDecorator(Color.RED, calendarDayList);
        materialCalendarView.addDecorators(eventDecorator);
    }


}
