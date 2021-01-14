"""学生成绩管理程序
编制一个统计学生考试分数的管理程序。
设学生成绩已以一个学生一个 记录 的形式存储在文件中，
每位学生记录包含的信息有：姓名，学号和各门功课的成绩。
程序具有以下几项功能：求出各门课程的总分，平均分，按姓名，
按学号寻找其记录并显示，浏览全部学生成绩和
按总分由高到低显示学生信息等。
"""

import os

swn = 3		# 课程数
#课程名称表
schoolwork = [ "Chinese", "Mathematic",
			   "English"]
# 各课程总分
total = [ 0 for i in range(swn)]

# 主要通过向 student_list列表 里存放 student字典，
# 向 student字典 里存放学生信息，相当于链表的一个节点，
#  student = {"name": ,"code": ,"marks":{ schoolwork[i]: ,}},
#  "marks"的值也是字典
student_list = []

def add_student():
	"""想要对文件录入的新的学生的记录"""
	new_students = []

	# n是我们需要录入文件的记录的“个数”
	n = int(input("Please input the record number "
				  "you want to write to the file: "))

	for i in range( 0, n):
		name = input("Input the student's name: ")
		code = input("Input the student's code: ")
		marks = {}
		for j in range( 0, swn):
			print("Input the %s mark: " % schoolwork[j], end = "")
			marks[ schoolwork[j]] = int( input())
		print("\n")

		student_record = {"name":name.title(), "code":code, "marks":marks}
		new_students.append(student_record)
	return new_students

def create_file( filename):
	"""
	创建日志文件夹和日志文件
	:param filename:
	:return:
	"""
	path = filename[0:filename.rfind("/")]
	if not os.path.isdir(path):  # 无文件夹时创建
		os.makedirs(path)
	if not os.path.isfile(filename):  # 无文件时创建
		fd = open(filename, mode="w", encoding="utf-8")
		fd.close()
	else:
		pass

def write_file( filename, new_students):
	"""将新输入的学生记录，录入文件"""
	fp = open(filename, "w", encoding='utf-8')
	fp.write(str(new_students))
	fp.close()
	print("Data saved to %s succeeded！" % filename)
	fp.close()

def read_file( filename):
	"""从文件中将数据读取至变量中"""
	global student_list

	if os.path.exists( filename):
		f = open( filename, 'r', encoding='utf-8')
		ret = f.read()
		student_list = eval(ret)
		f.close()

	for s in student_list:
		s["total"] = 0
		for value in s["marks"].values():
			s["total"] = s["total"] + value

	return

def total_mark():
	""" 计算各单科总分 """
	for i in range( 0, swn):
		total[i] = 0
	count = 0

	for s in student_list:
		for i in range( 0, swn):
			total[i] = total[i] + s["marks"][schoolwork[i]]
		count = count + 1

	return count	# 返回记录数

def display_stu( student):
	""" 显示学生记录 """
	print("Name   : ", student["name"])
	print("Code   : ", student["code"])
	print("Marks  :")
	for key,value in sorted( student["marks"].items()):
		print("       %-15s : %4d" % ( key, value))
	print("Total  : %4d" % student["total"])

def list_stu():
	"""列表显示学生信息 """
	for s in student_list:
		display_stu(s)
		print("\n\t Press Blank "
			  "space to continue...")
		e = input()
		while (e != ' '):
			e = input()
	print("\n\t END")

def display_list():
	""" 总分从大到小顺序显示链表各表元"""
	#列表中镶嵌着字典用这种sorted()
	for s in sorted(student_list, key=lambda
			k: k["total"], reverse=True):
		display_stu(s)
		print("\n\t Press Blank "
			  "space to continue...")
		e = input()
		while (e != ' '):
			e = input()
	print("\n\t END")

def show_list():
	""" 菜单"""
	print("\nNow you can input a command to manage the records.")
	print("m : mean of the marks.") #平均分数
	print("t : total of the marks.") #计算总分
	print("n : search record by student's name.") #按学生姓名搜索记录
	print("c : search record by student's code.") #按学生学号搜索记录
	print("l : list all the records.") #列出所有记录
	print("s : sort and list the records by the total.") #按总数排序和列出记录
	print("q : quit!\n") #退出

def retrieve_n( key):
	"""按学生姓名查找学生记录"""
	c = 0
	for s in student_list:
		if key == s["name"]:
			display_stu(s)
			c = c + 1
	if c == 0:
		print("The student %s is not "
			  "in the file." % key)
	return 1

def retrieve_c( key):
	"""按学生学号查找学生记录"""
	c = 0
	for s in student_list:
		if key == s["code"]:
			display_stu(s)
			c = c + 1
			break
	if c == 0:
		print("The student %s is not "
			  "in the file." % key)
	return 1

def main():
	os.system("cls")
	stuf = input("Please input the students "
				 "marks record file's name: ") #文件名

	#调用函数“检查学生成绩记录文件是否创建”
	if not os.path.exists(stuf):
		c = input("The file %s doesn't exit,"
				  "do you want to creat it? (Y/N) ")
		if c == 'Y' or c == 'y':
			create_file( stuf)
			write_file( stuf, add_student())
		else:
			print("\n\tThank you for your using.")
			return

	#学生的信息都将存入student_list中
	read_file( stuf)

	if not student_list:
		print("\n\tStudent information is not stored in the file.")
		print("\t\t\tThank you for your using.")
		return

	i = input("Press any key to clear the "
			  "screen, then the menu will appear")
	os.system("cls")

	while 1:
		show_list()
		c = input("\nPlease input command:") # 输入选择命令
		os.system("cls")
		if c == 'q' or c == 'Q':
			print("\n\tThank you for your using.")
			break

		# 计算单科平均分
		elif c == 'm' or c == 'M':
			n = total_mark()
			if n == 0:
				print("\nError!")
				continue

			print("")
			for i in range( 0, swn):
				print("%-15s's average is: %.2f." %
					  ( schoolwork[i],
						(float)(total[i] / n)) )

		# 计算单科总分
		elif c == 't' or c == 'T':
			if total_mark() == 0:
				print("Error!")
				continue

			print("")
			for i in range( 0, swn):
				print("%-15s's total mark is: %d." %
					  (schoolwork[i], total[i]) )

		#按学生的姓名寻找记录
		elif c == 'n' or c == 'N':
			buf = input("Please input the student's "
				   "name you want to search: ")
			retrieve_n(buf.title())

		# 按学生的学号寻找记录
		elif c == 'c' or c == 'C':
			buf = input("Please input the student's "
				   "code you want to search: ")
			retrieve_c(buf)

		# 列出所有学生记录
		elif c == 'l' or c == 'L':
			list_stu()

		# 按总分从高到低排列显示
		elif c == 's' or c == 'S':
			display_list()

		else:
			print("Input command error, please input again")

main()

