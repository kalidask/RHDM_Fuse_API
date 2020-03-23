package org.mycompany;

import java.util.*;
import org.apache.camel.Exchange;
import com.jayway.jsonpath.JsonPath;

public class conditionprocessor {

	public static void validateFuseCondition(Exchange exchange) {
		String body = exchange.getIn().getBody(String.class);
		List<String> memberList = JsonPath.read(body, "$['policydolist'][0]['insureddolist'][*]");
		int sizeMember = memberList.size();

		String result = null;

		String homeresult1 = null;

		String videoresult1 = null;

		String teleresult1 = null;

		ArrayList<Integer> loadinglist = new ArrayList<>();

		ArrayList<String> result1 = new ArrayList<String>();

		ArrayList<String> homeresult = new ArrayList<String>();

		ArrayList<String> videoresult = new ArrayList<String>();

		ArrayList<String> reasonlist = new ArrayList<String>();

		List<Map<String, Object>> policylist = new ArrayList<Map<String, Object>>();

		List<Map<String, Object>> insuredresponselist = new ArrayList<Map<String, Object>>();

		HashMap<String, Object> policymap = new HashMap<String, Object>();

		HashMap<String, Object> policydomap = new HashMap<String, Object>();

		for (int i = 0; i < sizeMember; i++) {

			Map<String, Object> map = JsonPath.read(body, "$['policydolist'][0]['insureddolist'][" + i + "]");

			int age = 0;

			if (map.get("entryage") != null && !map.get("entryage").toString().isEmpty()) {
				try {
					age = (int) map.get("entryage");
				} catch (Exception e) {
					String xage = (String) map.get("entryage");
					age = Integer.parseInt(xage);
				}
			}

			//System.out.println("entryage:--" + age);

			double bmi = 0;

			if (map.get("bmi") != null && !map.get("bmi").toString().isEmpty()) {
				try {
					bmi = (Double) map.get("bmi");
				} catch (Exception e) {
					int x = (int) map.get("bmi");
					bmi = Double.valueOf(x);
				}
			}

			//System.out.println("bmi:----" + bmi);

			String insuredid = (String) map.get("insuredid");

			//System.out.println("insureID:--" + insuredid);

			List<Map<String, Object>> mainlist = JsonPath.read(body, "$['policydolist']");
			HashMap<String, Object> mainhash = (HashMap<String, Object>) mainlist.get(0);

			String planoptioncd = (String) mainhash.get("planoptioncd");
			String minunitscd = (String) mainhash.get("minunitscd");
			String inwardtypecd = (String) mainhash.get("inwardtypecd");

			//System.out.println("planoptioncd:--" + planoptioncd);
			//System.out.println("minunitscd:--" + minunitscd);
			//System.out.println("inwardtypecd:--" + inwardtypecd);

			Map<String, Object> medicallist = JsonPath.read(body,
					"$['policydolist'][0]['insureddolist'][" + i + "]['medicalquestions'][0]['disease']");

			String illness = (String) medicallist.get("illness");

			//System.out.println("illness:--" + illness);

			String dateofdiagnosis = (String) medicallist.get("dateofdiagnosis");

			//System.out.println("dateofdiagnosis:--" + dateofdiagnosis);

			/* Response map and arraylists */

			HashMap<String, Object> astp = new HashMap<String, Object>();

			HashMap<String, Object> ppmc = new HashMap<String, Object>();

			HashMap<String, Object> medical = new HashMap<String, Object>();

			HashMap<String, Object> teleuw = new HashMap<String, Object>();

			HashMap<String, Object> nomatch = new HashMap<String, Object>();

			HashMap<String, Object> insuredolistmap = new HashMap<>();

			HashMap<String, Object> ruledoc = new HashMap<String, Object>();

			List<Map<String, Object>> doclist = new ArrayList<Map<String, Object>>();

			HashMap<String, Object> memberdoc = new HashMap<String, Object>();

			List<Map<String, Object>> memberdoclist = new ArrayList<Map<String, Object>>();

			List<Map<String, Object>> rulelist = new ArrayList<Map<String, Object>>();

			//System.out.println("before if condition");
			if (age >= 16 && bmi >= 29.01 && bmi <= 31) {

				System.out.println("entryage:--" + map.get("entryage") + "bmi:----" + map.get("bmi"));

				astp.put("ruledecision", "STP");
				astp.put("priority", "");
				astp.put("pppmcrequired", "");
				astp.put("reason", "High body mass index");

				astp.put("loading", "10%");
				astp.put("testset_1", "");
				astp.put("testset_2", "");

				ruledoc.put("doc1", "");
				ruledoc.put("doc2", "");

				doclist.add(ruledoc);
				astp.put("listofdocumentsneeded", doclist);
				astp.put("rulename", "BMI");

				rulelist.add(astp);

				insuredolistmap.put("rule", rulelist);
				insuredolistmap.put("insuredid", insuredid);

				insuredolistmap.put("insureddecision", "STP");
				insuredolistmap.put("memberloading", "10%");
				insuredolistmap.put("videoeligibility", "No");
				insuredolistmap.put("homeeligibility", "No");
				insuredolistmap.put("teleuw", "No");
				insuredolistmap.put("testset_1", "");

				memberdoc.put("doc1", "");
				memberdoc.put("doc2", "");

				memberdoclist.add(ruledoc);
				insuredolistmap.put("listofdocumentsneeded", memberdoclist);

				insuredresponselist.add(insuredolistmap);

				loadinglist.add(10);

				result1.add("STP");

				homeresult.add("No");
				videoresult.add("No");
				teleresult1 = "No";

				reasonlist.add("BMI");

			} else if (illness.equals("Hypertension") && dateofdiagnosis.equals(">20 years")) {

				medical.put("ruledecision", "decline");
				medical.put("reason", " H/O Hypertension");
				medical.put("loading", "0%");
				medical.put("testset_1", "");
				medical.put("testset_2", "");
				medical.put("priority", "");
				medical.put("pppmcrequired", "");

				ruledoc.put("doc1", "");
				ruledoc.put("doc2", "");

				doclist.add(ruledoc);
				medical.put("listofdocumentsneeded", doclist);

				medical.put("rulename", "Medical Rule");

				rulelist.add(medical);

				insuredolistmap.put("rule", rulelist);
				insuredolistmap.put("insuredid", insuredid);
				insuredolistmap.put("insureddecision", "decline");
				insuredolistmap.put("memberloading", "0%");
				insuredolistmap.put("videoeligibility", "No");
				insuredolistmap.put("homeeligibility", "No");
				insuredolistmap.put("teleuw", "No");
				insuredolistmap.put("testset_1", "");

				memberdoc.put("doc1", "");
				memberdoc.put("doc2", "");

				memberdoclist.add(ruledoc);
				insuredolistmap.put("listofdocumentsneeded", memberdoclist);

				insuredresponselist.add(insuredolistmap);

				result1.add("decline");
				homeresult.add("No");
				videoresult.add("No");
				teleresult1 = "No";

				reasonlist.add("H/O Hypertension");

			} else if (age >= 51 && age <= 120 && planoptioncd.equals("FL-PRT10-HMB500") && minunitscd.equals("Years")
					&& inwardtypecd.equals("NEWBUSINESS")) {
				ppmc.put("ruledecision", "NSTP");
				ppmc.put("testset_1", "Set_0011");
				ppmc.put("testset_2", "");

				ppmc.put("priority", "3");
				ppmc.put("reason", "PPMC rule");

				ppmc.put("loading", "0%");

				ppmc.put("pppmcrequired", "");

				ruledoc.put("doc1", "");
				ruledoc.put("doc2", "");

				doclist.add(ruledoc);
				ppmc.put("listofdocumentsneeded", doclist);

				ppmc.put("rulename", "PPMC Rule");

				rulelist.add(ppmc);

				insuredolistmap.put("rule", rulelist);
				insuredolistmap.put("insuredid", insuredid);
				insuredolistmap.put("insureddecision", "NSTP");
				insuredolistmap.put("loading", " ");

				insuredolistmap.put("memberloading", "0%");
				insuredolistmap.put("videoeligibility", "No");
				insuredolistmap.put("homeeligibility", "Yes");
				insuredolistmap.put("teleuw", "No");
				insuredolistmap.put("testset_1", "Set_0011");

				memberdoc.put("doc1", "");
				memberdoc.put("doc2", "");

				memberdoclist.add(ruledoc);
				insuredolistmap.put("listofdocumentsneeded", memberdoclist);

				insuredresponselist.add(insuredolistmap);

				result1.add("NSTP");

				homeresult.add("Yes");
				videoresult.add("No");
				teleresult1 = "No";

				reasonlist.add("PPMC rule");

			} else if (age >= 46 && age <= 50 && planoptioncd.equals("FL-PRT10-HMB500") && minunitscd.equals("Years")
					&& inwardtypecd.equals("NEWBUSINESS")) {

				teleuw.put("ruledecision", "STP");
				teleuw.put("reason", "Tele UW Rule");

				teleuw.put("testset_1", "");
				teleuw.put("testset_2", "");

				teleuw.put("priority", "");

				teleuw.put("loading", "0%");

				teleuw.put("pppmcrequired", "");

				ruledoc.put("doc1", "");
				ruledoc.put("doc2", "");

				doclist.add(ruledoc);
				teleuw.put("listofdocumentsneeded", doclist);

				teleuw.put("rulename", "Tele UW");

				rulelist.add(teleuw);

				insuredolistmap.put("rule", rulelist);
				insuredolistmap.put("insuredid", insuredid);
				insuredolistmap.put("insureddecision", "STP");

				insuredolistmap.put("memberloading", "0%");
				insuredolistmap.put("videoeligibility", "Yes");
				insuredolistmap.put("homeeligibility", "No");
				insuredolistmap.put("teleuw", "No");
				insuredolistmap.put("testset_1", "");

				memberdoc.put("doc1", "");
				memberdoc.put("doc2", "");

				memberdoclist.add(ruledoc);
				insuredolistmap.put("listofdocumentsneeded", memberdoclist);

				insuredresponselist.add(insuredolistmap);

				result1.add("STP");
				homeresult.add("No");
				videoresult.add("Yes");
				teleresult1 = "No";

				reasonlist.add("Tele UW Rule");

			} else

			{

				nomatch.put("ruledecision", "STP");
				nomatch.put("reason", "No Rule Condition Match");

				nomatch.put("testset_1", "");
				nomatch.put("testset_2", "");

				nomatch.put("priority", "");

				nomatch.put("loading", "0%");

				nomatch.put("pppmcrequired", "");

				ruledoc.put("doc1", "");
				ruledoc.put("doc2", "");

				doclist.add(ruledoc);
				nomatch.put("listofdocumentsneeded", doclist);

				nomatch.put("rulename", "No Rule");

				rulelist.add(nomatch);

				insuredolistmap.put("rule", rulelist);
				insuredolistmap.put("reason", "No Rule Condition Match");
				insuredolistmap.put("insuredid", insuredid);
				insuredolistmap.put("insureddecision", "STP");

				insuredolistmap.put("memberloading", "0%");
				insuredolistmap.put("videoeligibility", "Yes");
				insuredolistmap.put("homeeligibility", "No");
				insuredolistmap.put("teleuw", "No");
				insuredolistmap.put("testset_1", "");

				memberdoc.put("doc1", "");
				memberdoc.put("doc2", "");

				memberdoclist.add(memberdoc);
				insuredolistmap.put("listofdocumentsneeded", memberdoclist);
				insuredresponselist.add(insuredolistmap);

				result1.add("STP");

				homeresult.add("No");
				videoresult.add("Yes");
				teleresult1 = "No";

				reasonlist.add("No Rule Condition Match");
			}
		}

		if (result1 != null && !result1.isEmpty()) {

			for (int i = 0; i < result1.size(); i++) {

				if (result1.get(i).contains("NSTP")) {

					result = "NSTP";
					break;

				}
				if (i == result1.size() - 1 && result == null) {
					result = "STP";

				}

			}
		}

		if (homeresult != null && !homeresult.isEmpty()) {

			for (int i = 0; i < homeresult.size(); i++) {

				if (homeresult.get(i).contains("Yes")) {
					homeresult1 = "Yes";
				}

				if (i == homeresult.size() - 1 && homeresult1 == null) {
					homeresult1 = "No";
				}

			}
		}

		if (videoresult != null && !videoresult.isEmpty()) {

			for (int i = 0; i < videoresult.size(); i++) {

				if (videoresult.get(i).contains("Yes")) {
					videoresult1 = "Yes";
				}

				if (i == videoresult.size() - 1 && videoresult1 == null) {
					videoresult1 = "No";
				}

			}
		}

		String reason = String.join(", ", reasonlist);

		policymap.put("insureddolist", insuredresponselist);

		policymap.put("policydecision", result);

		policymap.put("homeeligibility", homeresult1);
		policymap.put("videoeligibility", videoresult1);
		policymap.put("teleUW", teleresult1);

		policymap.put("decisionreason", reason);

		Integer finalloading = 0;
		for (int i : loadinglist) {
			finalloading += i;

		}

		policymap.put("policyloading", finalloading.toString() + "%");

		policylist.add(policymap);

		policydomap.put("policydolist", policylist);

		exchange.getOut().setBody(policydomap);

	}

}
