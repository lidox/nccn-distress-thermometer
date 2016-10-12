package com.artursworld.nccn;

import android.content.Context;

import com.tech.freak.wizardpager.model.AbstractWizardModel;
import com.tech.freak.wizardpager.model.BranchPage;
import com.tech.freak.wizardpager.model.MultipleFixedChoicePage;
import com.tech.freak.wizardpager.model.NumberPage;
import com.tech.freak.wizardpager.model.PageList;
import com.tech.freak.wizardpager.model.SingleFixedChoicePage;
import com.tech.freak.wizardpager.model.TextPage;

public class SandwichWizardModel extends AbstractWizardModel {
    public SandwichWizardModel(Context context) {
        super(context);
    }

    @Override
    protected PageList onNewRootPageList() {
        return new PageList(new BranchPage(this, "Order type").addBranch(
                "Sandwich",
                new SingleFixedChoicePage(this, "Bread").setChoices("White",
                        "Wheat", "Rye", "Pretzel", "Ciabatta")
                        .setRequired(true),

                new MultipleFixedChoicePage(this, "Meats").setChoices(
                        "Pepperoni", "Turkey", "Ham", "Pastrami", "Roast Beef",
                        "Bologna"),

                new MultipleFixedChoicePage(this, "Veggies").setChoices(
                        "Tomatoes", "Lettuce", "Onions", "Pickles",
                        "Cucumbers", "Peppers"),

                new MultipleFixedChoicePage(this, "Cheeses").setChoices(
                        "Swiss", "American", "Pepperjack", "Muenster",
                        "Provolone", "White American", "Cheddar", "Bleu"),

                new BranchPage(this, "Toasted?")
                        .addBranch(
                                "Yes",
                                new SingleFixedChoicePage(this, "Toast time")
                                        .setChoices("30 seconds", "1 minute",
                                                "2 minutes")).addBranch("No")
                        .setValue("No"))

                .addBranch(
                        "Salad",
                        new SingleFixedChoicePage(this, "Salad type").setChoices(
                                "Greek", "Caesar").setRequired(true),

                        new SingleFixedChoicePage(this, "Dressing").setChoices(
                                "No dressing", "Balsamic", "Oil & vinegar",
                                "Thousand Island", "Italian").setValue("No dressing"),
                        new NumberPage(this, "How Many Salads?").setRequired(true)),
                new TextPage(this, "Comments").setRequired(true)

                        .setRequired(true),

                new CustomerInfoPage(this, "Your info").setRequired(true));
    }
}
