package services;

import views.common.components.EditableLabelValue;
import views.common.components.LabelValue;

import javax.swing.JLabel;
import java.util.ArrayList;
import java.util.List;

public class ViewService {

    public static List<Integer> getLabelValueOffsets(List<JLabel> labelList) {
        List<Integer> offsets = new ArrayList<>();
        int max = 0;

        for (JLabel label : labelList) {
            max = label.getWidth() > max ? label.getWidth() : max;
        }

        for (JLabel label : labelList) {
            int offset = max - label.getWidth();
            offsets.add(offset);
        }

        return offsets;
    }

    public static List<LabelValue> updateLabelValueOffsets(List<LabelValue> labelValueList, boolean isEditable) {
        List<LabelValue> updatedSet = new ArrayList<>();
        List<JLabel> labels = getLabelList(labelValueList);
        List<Integer> offsets = getLabelValueOffsets(labels);

        for (int i=0; i<labelValueList.size(); i++) {
            LabelValue current = labelValueList.get(i);
            if (isEditable) {
                updatedSet.add(new EditableLabelValue(
                        current.getLabel(),
                        current.getValue(),
                        offsets.get(i)
                ));
            } else {
                updatedSet.add(new LabelValue(
                        current.getLabel(),
                        current.getValue(),
                        offsets.get(i)
                ));
            }
        }

        return updatedSet;
    }

    private static List<JLabel> getLabelList(List<LabelValue> labelValueList) {
        List<JLabel> labels = new ArrayList<>();

        for (LabelValue labelValue : labelValueList) {
            labels.add(labelValue.getLabel());
        }

        return labels;
    }
}
