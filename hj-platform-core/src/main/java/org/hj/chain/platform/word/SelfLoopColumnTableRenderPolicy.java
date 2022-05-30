package org.hj.chain.platform.word;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.plugin.table.LoopColumnTableRenderPolicy;
import com.deepoove.poi.template.ElementTemplate;
import com.deepoove.poi.template.run.RunTemplate;
import com.deepoove.poi.util.TableTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 自定义循环列表
 * @Iteration : 1.0
 * @Date : 2022/5/21  10:17 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2022/05/21    create
 */
@Slf4j
public class SelfLoopColumnTableRenderPolicy extends LoopColumnTableRenderPolicy {
    private String prefix;
    private String suffix;
    private boolean onSameLine;
    //合并列
    private int[] mergeRows;
    //开始列
    private int startColumn;
    //结束列
    private int endColumn;

    public SelfLoopColumnTableRenderPolicy(int startColumn, int endColumn, int[] mergeRows) {
        super();
        this.startColumn = startColumn;
        this.endColumn = endColumn;
        this.mergeRows = mergeRows;
    }

    public SelfLoopColumnTableRenderPolicy(boolean onSameLine) {
        super(onSameLine);
    }

    public SelfLoopColumnTableRenderPolicy(String prefix, String suffix) {
        super(prefix, suffix, false);
    }

    public SelfLoopColumnTableRenderPolicy(String prefix, String suffix, boolean onSameLine) {
        super(prefix, suffix, onSameLine);
    }

    @Override
    public void render(ElementTemplate eleTemplate, Object data, XWPFTemplate template) {
        super.render(eleTemplate, data, template);

        if (mergeRows != null && mergeRows.length > 0) {
            RunTemplate runTemplate = (RunTemplate) eleTemplate;
            XWPFRun run = runTemplate.getRun();
            try {
                XWPFTableCell tagCell = (XWPFTableCell) ((XWPFParagraph) run.getParent()).getBody();
                XWPFTable table = tagCell.getTableRow().getTable();

//                for (int i = 0; i < mergeRows.length; i++) {
//                    log.info("i=" + i + "  行：" + mergeRows[i]);
//                    TableTools.mergeCellsHorizonal(table, mergeRows[i], startColumn, endColumn);
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}