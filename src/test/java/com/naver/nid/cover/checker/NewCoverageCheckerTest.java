package com.naver.nid.cover.checker;

import com.naver.nid.cover.checker.model.NewCoverageCheckReport;
import com.naver.nid.cover.checker.model.NewCoveredFile;
import com.naver.nid.cover.parser.coverage.model.CoverageStatus;
import com.naver.nid.cover.parser.coverage.model.FileCoverageReport;
import com.naver.nid.cover.parser.coverage.model.LineCoverageReport;
import com.naver.nid.cover.parser.diff.model.Diff;
import com.naver.nid.cover.parser.diff.model.DiffSection;
import com.naver.nid.cover.parser.diff.model.Line;
import com.naver.nid.cover.parser.diff.model.ModifyType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NewCoverageCheckerTest {

	@Test
	public void coverCheckTest() {
		NewCoverageChecker checker = new NewCoverageChecker();

		List<Line> lines = Arrays.asList(
				Line.builder().lineNumber(1).type(ModifyType.ADD).build()
				, Line.builder().lineNumber(2).type(ModifyType.ADD).build());

		List<DiffSection> diffSectionList = Collections.singletonList(DiffSection.builder().lineList(lines).build());
		List<Diff> diffList = Arrays.asList(Diff.builder().fileName("src/main/java/pkgA/test.java").diffSectionList(diffSectionList).build(),
			Diff.builder().fileName("testModule/src/main/java/pkgB/test.java").diffSectionList(diffSectionList).build());


		LineCoverageReport lineCoverageReport = new LineCoverageReport();
		lineCoverageReport.setStatus(CoverageStatus.COVERED);
		lineCoverageReport.setLineNum(1);

		LineCoverageReport lineCoverageReport2 = new LineCoverageReport();
		lineCoverageReport2.setStatus(CoverageStatus.UNCOVERED);
		lineCoverageReport2.setLineNum(2);

		FileCoverageReport fileCoverageReport = new FileCoverageReport();
		fileCoverageReport.setType("java");
		fileCoverageReport.setFileName("pkgA/test.java");
		fileCoverageReport.setLineCoverageReportList(Arrays.asList(lineCoverageReport, lineCoverageReport2));

		FileCoverageReport fileCoverageReport2 = new FileCoverageReport();
		fileCoverageReport2.setType("java");
		fileCoverageReport2.setFileName("pkgB/test.java");
		fileCoverageReport2.setLineCoverageReportList(Arrays.asList(lineCoverageReport, lineCoverageReport2));

		List<FileCoverageReport> coverage = Arrays.asList(fileCoverageReport, fileCoverageReport2);

		NewCoverageCheckReport newCoverageCheckReport = NewCoverageCheckReport.builder()
				.threshold(60)
				.totalNewLine(4)
				.coveredNewLine(2)
				.coveredFilesInfo(
						Arrays.asList(NewCoveredFile.builder()
								.name("pkgA/test.java")
								.addedLine(2)
								.addedCoverLine(1)
								.build(),
							NewCoveredFile.builder()
								.name("pkgB/test.java")
								.addedLine(2)
								.addedCoverLine(1)
								.build()))
				.build();

		NewCoverageCheckReport check = checker.check(coverage, diffList, 60, 0);
		assertEquals(newCoverageCheckReport, check);

	}

}