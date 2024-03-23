package edu.whu.spacetime.activity;

import static com.xuexiang.xui.utils.StatusBarUtils.setStatusBarDarkMode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import edu.whu.spacetime.R;
import edu.whu.spacetime.SpacetimeApplication;
import edu.whu.spacetime.adapter.NoteListAdapter;
import edu.whu.spacetime.dao.NoteDao;
import edu.whu.spacetime.domain.Note;

public class UserCalendarActivity extends AppCompatActivity implements CalendarView.OnCalendarSelectListener,
        CalendarView.OnYearChangeListener,
        View.OnClickListener {

    TextView mTextMonthDay;

    TextView mTextYear;

    TextView mTextLunar;

    TextView mTextCurrentDay;

    CalendarView mCalendarView;

    RelativeLayout mRelativeTool;
    private int mYear;
    CalendarLayout mCalendarLayout;
    ListView mListView;
    private NoteDao noteDao;
    private List<Note> allNotes;
    private Map<String, List<Note>> notesMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_calendar);

        initView();
        initData();
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.tv_month_day) {
            if (!mCalendarLayout.isExpand()) {
                mCalendarLayout.expand();
                return;
            }
            mCalendarView.showYearSelectLayout(mYear);
            mTextLunar.setVisibility(View.GONE);
            mTextYear.setVisibility(View.GONE);
            mTextMonthDay.setText(String.valueOf(mYear));
            mCalendarView.getSelectedCalendar();
//            mListView.setAdapter(new NoteListAdapter(this, R.layout.item_note_list, notesMap.get()));
        }
        else if (vId == R.id.fl_current) {
            mCalendarView.scrollToCurrent();
        }
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
    }

    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
    }

    private void initView() {
        noteDao = SpacetimeApplication.getInstance().getDatabase().getNoteDao();
        mTextMonthDay = findViewById(R.id.tv_month_day);
        mTextYear = findViewById(R.id.tv_year);
        mTextLunar = findViewById(R.id.tv_lunar);
        mRelativeTool = findViewById(R.id.rl_tool);
        mCalendarView = findViewById(R.id.calendarView);
        mTextCurrentDay = findViewById(R.id.tv_current_day);
        mTextMonthDay.setOnClickListener(this);
        findViewById(R.id.fl_current).setOnClickListener(this);
        mCalendarLayout = findViewById(R.id.calendarLayout);
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnYearChangeListener(this);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));
    }

    public void initData() {
        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        int userId = SpacetimeApplication.getInstance().getCurrentUser().getUserId();
        allNotes = noteDao.queryByUserId(userId);

        notesMap = new HashMap<>();
        for (int i = 0; i < allNotes.size(); i ++ ) {
            Note now = allNotes.get(i);
            LocalDateTime noteTime = now.getCreateTime();
            String ymd = noteTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            Objects.requireNonNull(notesMap.get(ymd)).add(now);
        }

        Map<String, Calendar> map = new HashMap<>();
        for (Map.Entry<String, List<Note>> entry : notesMap.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue().size();
            int year = Integer.parseInt(key.substring(0, 4));
            int month = Integer.parseInt(key.substring(5, 7));
            int day = Integer.parseInt(key.substring(8, 10));
            String text = Integer.toString(value);
            Calendar calendar = getSchemeCalendar(year, month, day, getGradientColor(value), text);
            map.put(calendar.toString(), calendar);
        }

        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        mCalendarView.setSchemeDate(map);

        mListView = findViewById(R.id.listView);
        mListView.setAdapter(new NoteListAdapter(this, R.layout.item_note_list, Objects.requireNonNull(notesMap.get(today))));
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        return calendar;
    }

    private int getGradientColor(int index) {
        switch (index) {
            case 1:
                return R.color.one_note_color;
            case 2:
                return R.color.two_notes_color;
            case 3:
                return R.color.three_notes_color;
            case 4:
                return R.color.four_notes_color;
            case 5:
                return R.color.five_notes_color;
            case 6:
                return R.color.six_notes_color;
            case 7:
                return R.color.seven_notes_color;
            case 8:
                return R.color.eight_notes_color;
            case 9:
                return R.color.nine_notes_color;
            case 10:
                return R.color.ten_notes_color;
            default:
                return 0xFFFFFFFF;
        }
    }
}