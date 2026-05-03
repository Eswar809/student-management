import os
import re

directories = [
    'src/main/java/com/eswardev/studentmanagement/controller',
    'src/main/java/com/eswardev/studentmanagement/service'
]

for directory in directories:
    for filename in os.listdir(directory):
        if not filename.endswith('.java'):
            continue
        filepath = os.path.join(directory, filename)
        with open(filepath, 'r', encoding='utf-8') as f:
            content = f.read()
            
        if 'public interface' in content:
            continue
            
        classname = filename.replace('.java', '')
        
        if 'org.slf4j.Logger' not in content:
            import_stmt = 'import org.slf4j.Logger;\nimport org.slf4j.LoggerFactory;\n'
            last_import = content.rfind('import ')
            if last_import != -1:
                end_of_last_import = content.find(';\n', last_import) + 2
                content = content[:end_of_last_import] + import_stmt + content[end_of_last_import:]
            else:
                pkg_end = content.find(';\n') + 2
                content = content[:pkg_end] + import_stmt + content[pkg_end:]
            
            logger_stmt = f'\n\tprivate static final Logger logger = LoggerFactory.getLogger({classname}.class);\n'
            content = re.sub(rf'(public class {classname}.*?{{)', r'\1' + logger_stmt, content, count=1)
            
        # method matching across lines
        # we look for public [type] method(..) [throws ..] {
        pattern = re.compile(r'(public\s+(?:[\w<>,\[\]\?]+\s+)+(\w+)\s*\([^)]*\)(?:\s*throws\s+[\w,\s]+)?\s*\{)', re.MULTILINE)
        
        def repl(match):
            full_match = match.group(1)
            method_name = match.group(2)
            # if we already see logger in this block (heuristically), skip
            return full_match + f'\n\t\tlogger.info("Successfully executed {method_name} or operation related to it");'
            
        # split by '{' and check next lines? No, just re.sub and then remove duplicates if any.
        # Actually a safer way is to just do a one time replace:
        new_content = pattern.sub(repl, content)
        
        # To avoid doubling up where logger is already manually added:
        new_content = new_content.replace('{\n\t\tlogger.info("Successfully executed loadUserByUsername or operation related to it");\n\t\tlogger.info("Attempting to load', '{\n\t\tlogger.info("Attempting to load')
        
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(new_content)

print("Logging added.")
