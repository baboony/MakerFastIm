package com.maker.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TimerDto {

    private String start;

    private String end;

    private String label;

    private TimerDto() {
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    public static class Builder {
        private Date start;
        private Date end;
        private String format;
        private int type;//0日间隔 1星期间隔 2月间隔

        private Builder(Date start, Date end) {
            this.start = start;
            this.end = end;
        }

        public static Builder newBuilder(Date start, Date end) {
            Builder builder = new Builder(start, end);
            return builder;
        }

        public Builder setStart(Date start) {
            this.start = start;
            return this;
        }

        public Builder setEnd(Date end) {
            this.end = end;
            return this;
        }

        public Builder setFormat(String format) {
            this.format = format;
            return this;
        }

        public Builder setType(int type) {
            this.type = type;
            return this;
        }

        public List<TimerDto> build() {
            Date today = TimerUtils.zeroOfToday();
            if (start == null) {
                start = today;
            }
            if (end == null) {
                end = TimerUtils.addDay(today, 1);
            }

            List<TimerDto> dtos = new ArrayList<TimerDto>();
            Date next = new Date();
            while (true) {
                switch (type) {
                    case 0:
                        next = TimerUtils.addDay(start, 1);
                        if (StringUtils.isEmpty(format)) {
                            format = "MM-dd";
                        }
                        break;
                    case 1:
                        next = TimerUtils.addWeek(start, 1);
                        break;
                    case 2:
                        next = TimerUtils.addMonth(start, 1);
                        if (StringUtils.isEmpty(format)) {
                            format = "MM";
                        }
                        break;
                }

                if (next.getTime() > end.getTime()) {
                    break;
                }

                TimerDto d = new TimerDto();
                d.setStart(TimerUtils.format(start, TimerUtils.YYYYMMDDHHMMSS));
                d.setEnd(TimerUtils.format(next, TimerUtils.YYYYMMDDHHMMSS));
                d.setLabel(TimerUtils.format(start, format));
                dtos.add(d);
                start = next;
            }
            return dtos;
        }
    }
}
