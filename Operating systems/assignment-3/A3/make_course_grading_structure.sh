
#make coordinator user
sudo adduser --disabled-password --gecos automatic coordinator

#make ta's and students
for i in {1..3}
do
  sudo adduser --disabled-password --gecos automatic ta$i
  sudo adduser --disabled-password --gecos automatic student$i
done

#make groups
sudo groupadd ta
sudo groupadd student

#assign students and ta's to groups
for i in {1..3}
do
    sudo usermod -a -G ta ta$i
    sudo usermod -a -G student student$i
done

# Create directory structure
mkdir -p grades
#create fies with the grades
echo "A" > grades/student1
echo "B" > grades/student2
echo "C" > grades/student3

# Change ownership and permissions
sudo chown -R coordinator:ta grades
sudo chmod -R 770 grades
sudo chmod -R u+rwx,g+rwxs,o-rwx grades
sudo find grades -type f -exec chmod u=rw,g=r,o=r {} \;