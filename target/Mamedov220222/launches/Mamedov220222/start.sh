#!/bin/bash
exec 3>&1 4>&2
trap 'exec 2>&4 1>&3' 0 1 2 3
exec 1> >( tee -ia log.out) 2>&1

cd ../../1_D20,0deg0,2keV_FeCrNiTi150k
mpirun -np 16  --oversubscribe ../../bin/ubuntu.PPROJ/SDTrimSP.exe
cd ../2_D40,0deg30,0keV_FeCrNiTi7M
mpirun -np 16  --oversubscribe ../../bin/ubuntu.PPROJ/SDTrimSP.exe
cd ..
java -jar ISInCa.jar -c launches/Mamedov220222/isinca-Mamedov220222.xml