package com.jacksonasantos.travelplan.dao;

import com.jacksonasantos.travelplan.R;

public class SummaryTravelExpense {
    public int expense_type;
    public Double expected_value;
    public Double realized_value;

    public SummaryTravelExpense() {
    }

    public int getExpense_type_image( int expense_type ) {
        int draw;
        switch(expense_type) {
            case 0: draw = R.drawable.ic_supply; break;
            case 1: draw = R.drawable.ic_food; break;
            case 2: draw = R.drawable.ic_toll; break;
            case 3: draw = R.drawable.ic_tour; break;
            case 4: draw = R.drawable.ic_menu_accommodation; break;
            case 5: draw = R.drawable.ic_money_extra; break;
            case 6: draw = R.drawable.ic_menu_insurance; break;
            default: draw = R.drawable.ic_error; break;
        }
        return draw;
    }

    public int getExpense_type() { return expense_type; }
    public void setExpense_type(int expense_type) { this.expense_type = expense_type; }

    public Double getExpected_value() { return expected_value; }
    public void setExpected_value(Double expected_value) { this.expected_value = expected_value; }

    public Double getRealized_value() { return realized_value; }
    public void setRealized_value(Double realized_value) { this.realized_value = realized_value;}
}