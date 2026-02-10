package com.ajay._3.controller;

import java.util.List;
import java.util.Map;

import javax.print.attribute.standard.PageRanges;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ajay._2.entity.Employee;
import com.ajay._4.service.EmployeeManagementService;

@Controller
public class EmployeeOperationsController {

	@Autowired
	private EmployeeManagementService empService;

	@GetMapping("/")
	public String showWelcomePage() {
		System.out.println("showWelcomePage()");
		return "welcome";
	}

	// Get mode(G)
	@GetMapping("/report")
	public String getAllEmployees(@PageableDefault(page = 0, size=5, sort = "empname", direction = Sort.Direction.ASC) Pageable pageable,
			@ModelAttribute("emp") Employee emp,Map<String, Object> map) {
		System.out.println("===========Pageable Methods ==============");
		System.out.println("getPageNumber() "+pageable.getPageNumber());
		System.out.println("getPageSize() "+pageable.getPageSize());
		System.out.println("getSort() "+pageable.getSort());
		Sort sort = pageable.getSort();
		System.out.println("first() "+pageable.first());
		System.out.println("next() "+pageable.next());
		System.out.println("hasPrevious() "+pageable.hasPrevious());
		System.out.println("Sorting "+sort.ascending());
		System.out.println("getOffset() "+pageable.getOffset());
		System.out.println("\n");
		Page<Employee> page = empService.fetchAllEmployees(pageable);
		//employeesList.get
		System.out.println("=========Page[I] methods(2)===========");
		System.out.println("Page-getTotalElements() "+page.getTotalElements());
		System.out.println("Page-getTotalPages() "+page.getTotalPages());
		System.out.println("\n");
		System.out.println("=========Slice[I] methods(13)==========");
		System.out.println("getContent() "+page.getContent());
		System.out.println("getNumber() "+page.getNumber());
		System.out.println("getNumberOfElements() "+page.getNumberOfElements());
		System.out.println("page.getPageable() "+page.getPageable());
		System.out.println("getSize() "+page.getSize());
		Sort sort2 = page.getSort();
		System.out.println("getSort() "+sort2.toString());
		System.out.println("hasContent() "+page.hasContent());
		System.out.println("hasNext() "+page.hasNext());
		System.out.println("hasPrevious()"+page.hasPrevious());
		System.out.println("isFirst()"+page.isFirst());
		System.out.println("isLast() "+page.isLast());
		System.out.println("nextPageable() "+page.nextPageable());
		System.out.println("previousPageable() "+page.previousPageable());
		System.out.println("\n");
//		Sort by = Sort.by(Direction.ASC,"empName");
//		PageRequest pageRequest = PageRequest.of(0, 5,by);
//		map.put("pageData", pageRequest);
	
		map.put("pageData", page);
		return "show_emps_data";
	}

	//================ Deletion without using Redirection =============
	
//	@GetMapping("/delete")
//	public String removeRecordById(@RequestParam("empno") int no, Map<String,Object> map) {
//		String returnMsg = empService.deleteRecordById(no);
//		attrs.addFlashAttribute("returnMsg",returnMsg);
//		map.put("returnMsg", returnMsg);
//		return "delete_result";
//	}

	//================ Deletion using Redirection =============
	@GetMapping("/delete")
	public String removeRecordById(@RequestParam("empno") int no, RedirectAttributes attrs ) {
		String returnMsg = empService.deleteRecordById(no);
		attrs.addFlashAttribute("returnMsg",returnMsg);
		return "redirect:report";
	}
	
	@GetMapping("/update")
	public void editEmployeeById(@RequestParam("no") Employee empFromReq,
			@ModelAttribute("empData") Employee empToForm) {
		int empno = empFromReq.getEmpno();
		empToForm.setEmpno(empno);
		String empname = empFromReq.getEmpname();
		empToForm.setEmpname(empname);
		String job = empFromReq.getJob();
		empToForm.setJob(job);
		Double sal = empFromReq.getSal();
		empToForm.setSal(sal);
		Integer deptno = empFromReq.getDeptno();
		empToForm.setDeptno(deptno);
	}

	@PostMapping("/update")
	public String editEmployeeByFormById(@ModelAttribute("empData") Employee emp, RedirectAttributes attrs /*Model model*/) {
		try {
			//model.addAttribute("emp", List.of(emp));
			String returnMsg = empService.updateRecordById(emp, emp.getEmpno());
//			model.addAttribute("returnMsg",returnMsg);
//			return "update_result";
			attrs.addFlashAttribute("returnMsg",returnMsg);
			return "redirect:report";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	@GetMapping("/register")
	public String registerEmployee(@ModelAttribute("emp") Employee employee) {
		return "add_employee";
	}

	// without PRG pattern
//	@PostMapping("/register")
//	public String registerEmployeeByPost(@ModelAttribute("emp") Employee employee, Map<String, Object> map) {
//		String returnMsg = empService.saveEmployee(employee);
//		List<Employee> fetchAllEmployees = empService.fetchAllEmployees();
//		map.put("listOfEmployees", List.of(fetchAllEmployees));
//		map.put("returnMsg", returnMsg);
//		return "show_emps_data";
//	}

	// with PRG pattern, with out Shared Memory(RedirectAttributes).
	// Post mode(P)
//	@PostMapping("/register")
//	public String registerEmployeeByPost(@ModelAttribute("emp") Employee employee, Map<String, Object> map) {
//		String returnMsg = empService.saveEmployee(employee);
//		map.put("returnMsg", returnMsg);
//		return "redirect:report"; // redirect mode(R)
//	}

	// with PRG pattern, with Shared Memory(RedirectAttributes).
		// Post mode(P)
	@PostMapping("/register")
	public String registerEmployeeByPost(@ModelAttribute("emp") Employee employee, RedirectAttributes attrs) {
		String returnMsg = empService.saveEmployee(employee);
		attrs.addFlashAttribute("returnMsg", returnMsg);
		return "redirect:report"; // redirect mode(R)
	}
	
	@PostMapping("/search")
	public String showDynamicSearchEmp(@ModelAttribute("emp") Employee emp, Map<String,Object> map) {
		List<Employee> listOfEmployeeDynamicSearch = empService.showEmployeeDynamicSearch(emp);
		map.put("listOfEmployees", listOfEmployeeDynamicSearch);
		return "show_emps_data";
	}
}
