#include<bits/stdc++.h>
#include<dirent.h>
#include <windows.h>
using namespace std;
#define MUTATION_RATE 0.1
#define CROSSOVER_RATE 0.75
#define POP_SIZE 251
#define GENERATIONS 300
#define nm 5.0/* 3 */
#define nc 2.0/* 12 */
#define nc2 2.0
#define GENERATIONSt 1000
#define CROSSOVER_RATE2 0.75
#define MUTATION_RATE2 0.1
#define ENCODE_RATE 0.5
//tham so
double Ttongthe=72000;
string tenfile[110],hesosac[110];
const float Emax =10800,Emin=540,Pm=1,v=5;
double T=18000,U=5,Emc=108000;
//thong tin mang
double dis[505][505];
struct Sensor {
	double x,y,p,energy,hesosac,xacsuatsac;
};
int n;
vector<Sensor> sensors;
//end
struct Individual{
	vector<double> mhpath;
	vector<int> path;
	vector<double> chargetime;
	double soluongnutchet;
	double fitness;
	double tsac;
	double tdichuyen;
	double thoidiemchetdautien;
void check() {
cout<<"STT"<<"|"<<"nangluong_bandau"<<"|"<<"congsuat"<<"|"<<"thoigianchosac"<<"|"<<"nangluong_batdausac"<<"|"<<"thoigiansac"<<"|"<<"nangluongsaukhisac"<<"|"<<"nangluongT"<<endl;
	  double dichuyen=0,sac=0;
	  vector<bool> charged(n+2,0);
	  cout<<endl<<setprecision(7)<<fixed;
	  for(int i=1;i<path.size()-1;i++) {
	  	dichuyen+=dis[path[i]][path[i-1]]/v;
	  	if(dichuyen+sac>T) break;
		  charged[path[i]]=1;
cout<<path[i]<<"|"<<sensors[path[i]].energy<<"|"<<sensors[path[i]].p<<"|"<<dichuyen+sac<<"|"<<sensors[path[i]].energy-sensors[path[i]].p*(dichuyen+sac)<<"|"<<chargetime[i]<<"|"<<sensors[path[i]].energy-sensors[path[i]].p*(dichuyen+sac)+chargetime[i]*(U-sensors[path[i]].p)<<"|"<<sensors[path[i]].energy-sensors[path[i]].p*(T)+chargetime[i]*(U);
	    if(sensors[path[i]].energy-sensors[path[i]].p*(dichuyen+sac)<=Emin||sensors[path[i]].energy-sensors[path[i]].p*(T)+chargetime[i]*(U)<=Emin) cout<<"        CHET";
	sac+=chargetime[i];
	cout<<endl;
	}
	for(int i=1;i<=n;i++) {
		if(charged[i]==1) continue;
		cout<<i<<"|"<<sensors[i].energy<<"|"<<sensors[i].p<<"|"<<sensors[i].energy-sensors[i].p*T;
		if(sensors[i].energy-sensors[i].p*T<Emin) cout<<"            CHET";
		cout<<endl;
	}
}
void check_path() {
	for(int i=0;i<path.size();i++) {
		cout<<path[i]<<" ";
	}
	cout<<endl;
}
void check_chargetime() {
	for(int i=0;i<chargetime.size();i++) {
		cout<<chargetime[i]<<" ";
	}
	cout<<endl;
};
};
struct Population{
	vector<Individual> pop;
	Individual best;
};
Population collect;
int random(int minN, int maxN){
    return minN + rand() % (maxN + 1 - minN);
}
double double_rand( double min, double max ) {
    double scale = abs(rand()) / (double) RAND_MAX; /* [0, 1.0] */
    return min + scale * ( max - min );      /* [min, max] */
}
void readfile(int qwer, int seed, double multi) {
	cout<<tenfile[qwer]<<" "<<seed<<endl;
	srand(seed);
	string s=tenfile[qwer];
	ifstream itt(s.c_str());
	if (itt.fail()) cout << "Failed to open this file!!" <<endl;
	int sodong=0;string q;char c;
	while (itt) {
	 int dem=0;
     while (itt && c != '\n') {
       itt.get(c);
       dem++;
     }
     if(dem>=2) sodong += 1;
     itt.get(c);
    }
    n=sodong-1;
	collect.pop.clear();
	sensors.clear();
	sensors.resize(n+1);
	ifstream it(s.c_str());
	if (it.fail()) cout << "Failed to open this file!!" <<endl;
	it>>sensors[0].x>>sensors[0].y;
	double max_p=0;
	for(int i=1;i<=n;i++) {
		it>>sensors[i].x>>sensors[i].y>>sensors[i].p>>sensors[i].energy;
		sensors[i].p *= multi;
		if(sensors[i].p>max_p) max_p=sensors[i].p;
	}
	it.close();
	for(int i=0;i<n+1;i++) {
		for(int j=0;j<n+1;j++) {
			dis[i][j]=sqrt(pow(sensors[i].x-sensors[j].x,2)+pow(sensors[i].y-sensors[j].y,2));
			if(i==j) {dis[i][j]=999999.0;continue;}
		}
	}
	/*ifstream ittt(hesosac[qwer].c_str());
	if(ittt) {
		for(int i=1;i<=n;i++) ittt>>sensors[i].hesosac;
	}*/

	for(int i=1;i<=n;i++) sensors[i].hesosac=sensors[i].p/(max_p*1.05);
}
vector<int> encode(vector<double> en) {
	vector<int> result;
	vector<vector<double> > ran_array;
	vector<double> qq(2);
	for(int i=1;i<=n;i++) {
		if(en[i]<sensors[i].xacsuatsac) {
			qq[0]=en[i];
			qq[1]=(double)i;
			ran_array.push_back(qq);
		}
	}
	for(int i=0;i<ran_array.size();i++) {
		for(int j=i+1;j<ran_array.size();j++) {
			if(ran_array[i][0]<ran_array[j][0]) swap(ran_array[i],ran_array[j]);
		}
	}
	result.push_back(0);
	for(int i=0;i<ran_array.size();i++) {
	result.push_back((int)ran_array[i][1]);
    }
    result.push_back(0);
    return result;
}
Individual init_fuzzy(Individual ob) {
	ob.chargetime.resize(ob.path.size());
	ob.chargetime[0]=0;
	ob.chargetime[ob.path.size()-1]=0;
	double dichuyen=0,sac=0,cc=0;
	for(int i=1;i<ob.path.size()-1;i++) {
		dichuyen+=dis[ob.path[i]][ob.path[i-1]]/v;
		cc+=sensors[ob.path[i]].hesosac;
	}
	dichuyen+=dis[ob.path[ob.path.size()-2]][0]/v;
	ob.tdichuyen=dichuyen;
	ob.tsac=(Emc-ob.tdichuyen*Pm)/U;
	for(int i=1;i<ob.path.size()-1;i++) {
		ob.chargetime[i]=(sensors[ob.path[i]].hesosac/cc)*ob.tsac;
	}
return ob;
}

Individual output(Individual ob) {
	double dichuyen=0,sac=0,cc=0,sonutchet=0,thoidiemchetdautien=999999;
	vector<int> t1(n+1,0);
	vector<int> t2(n+1,0);
	vector<double> thoidiemchet(n+1,999999);
	vector<bool> charged(n+2,0);
	for(int i=1;i<ob.path.size()-1;i++) {
		dichuyen+=dis[ob.path[i]][ob.path[i-1]]/v;
		if(dichuyen+sac>T) {dichuyen=T-sac;break;};
		charged[ob.path[i]]=1;
		if(sensors[ob.path[i]].energy-(dichuyen+sac)*sensors[ob.path[i]].p<Emin)  {t1[ob.path[i]]=1;thoidiemchet[ob.path[i]]=(sensors[ob.path[i]].energy-Emin)/sensors[ob.path[i]].p;}
		if(sensors[ob.path[i]].energy-(dichuyen+sac)*sensors[ob.path[i]].p+ob.chargetime[i]*(U-sensors[ob.path[i]].p)>Emax) {
			double ttt=-Emax+sensors[ob.path[i]].energy-(dichuyen+sac)*sensors[ob.path[i]].p+ob.chargetime[i]*(U-sensors[ob.path[i]].p);
			ttt/=abs(U-sensors[ob.path[i]].p);
			ob.tsac-=(ttt);
			ob.chargetime[i]-=ttt;
		}
		if(t1[ob.path[i]]==1) {ob.chargetime[i]=0;continue;} else {
		sac+=ob.chargetime[i];
		if(dichuyen+sac>T) {
		double hh=dichuyen+sac-T;
		ob.chargetime[i]-=hh;
		sac-=hh;
		break;
	    }
	    }
	    if(i==ob.path.size()-2) dichuyen+=dis[ob.path[i]][0]/v;
	}
	for(int i=1;i<ob.path.size()-1&&charged[ob.path[i]]==1;i++) {
		if(sensors[ob.path[i]].energy-(T)*sensors[ob.path[i]].p+ob.chargetime[i]*U<Emin) {
		        t2[ob.path[i]]=1; 
			    if(thoidiemchet[ob.path[i]]==999999) {
			        thoidiemchet[ob.path[i]]=(sensors[ob.path[i]].energy+ob.chargetime[i]*U-Emin)/sensors[ob.path[i]].p;
			    }
		}
	}
	for(int i=1;i<=n;i++) {
		if(charged[i]==1) continue;
		if(sensors[i].energy-(T)*sensors[i].p<Emin) {t2[i]=1;thoidiemchet[i]=(sensors[i].energy-Emin)/sensors[i].p;}
	}
	for(int i=1;i<=n;i++) {
		if(t1[i]==1||t2[i]==1) sonutchet++;
		if(thoidiemchet[i]<thoidiemchetdautien) thoidiemchetdautien=thoidiemchet[i];
    }

    ob.soluongnutchet=sonutchet;    
    ob.thoidiemchetdautien=thoidiemchetdautien;
    ob.soluongnutchet=sonutchet;
    ob.tdichuyen=dichuyen;
    ob.tsac=sac;
    
    //ob.fitness=ob.soluongnutchet/n+abs(Pm*ob.tdichuyen)/Emc;
    ob.fitness=ob.soluongnutchet/n-abs(U*ob.tsac)/Emc;
    
    return ob;
}

//fitness
Individual init_fitness(Individual ob) {
	double dichuyen=0,sac=0,cc=0,sonutchet=0,thoidiemchetdautien=999999;
	Individual xx=ob;
	vector<int> t1(n+1,0);
	vector<int> t2(n+1,0);
	vector<double> thoidiemchet(n+1,999999);
	vector<bool> charged(n+2,0);
	for(int i=1;i<ob.path.size()-1;i++) {
		dichuyen+=dis[ob.path[i]][ob.path[i-1]]/v;
		if(dichuyen+sac>T) {dichuyen=T-sac;break;};
		charged[ob.path[i]]=1;
		if(sensors[ob.path[i]].energy-(dichuyen+sac)*sensors[ob.path[i]].p<Emin) {t1[ob.path[i]]=1;thoidiemchet[ob.path[i]]=(sensors[ob.path[i]].energy-Emin)/sensors[ob.path[i]].p;}
		if(sensors[ob.path[i]].energy-(dichuyen+sac)*sensors[ob.path[i]].p+ob.chargetime[i]*(U-sensors[ob.path[i]].p)>Emax) {
			double ttt=-Emax+sensors[ob.path[i]].energy-(dichuyen+sac)*sensors[ob.path[i]].p+ob.chargetime[i]*(U-sensors[ob.path[i]].p);
			ttt/=abs(U-sensors[ob.path[i]].p);
			ob.tsac-=(ttt);
			ob.chargetime[i]-=ttt;
		}
		if(t1[ob.path[i]]==1) {ob.chargetime[i]=0;continue;} else {
		sac+=ob.chargetime[i];
		if(dichuyen+sac>T) {
		double hh=dichuyen+sac-T;
		ob.chargetime[i]-=hh;
		sac-=hh;
		break;
	    }
	    }
	    if(i==ob.path.size()-2) dichuyen+=dis[ob.path[i]][0]/v;
	}
	for(int i=1;i<ob.path.size()-1&&charged[ob.path[i]]==1;i++) {
		if(sensors[ob.path[i]].energy-(T)*sensors[ob.path[i]].p+ob.chargetime[i]*U<Emin) {
	    	t2[ob.path[i]]=1; 
	    	if(thoidiemchet[ob.path[i]]==999999) {
			    thoidiemchet[ob.path[i]]=(sensors[ob.path[i]].energy+ob.chargetime[i]*U-Emin)/sensors[ob.path[i]].p;
			}
		}
	}
	for(int i=1;i<=n;i++) {
		if(charged[i]==1) continue;
		if(sensors[i].energy-(T)*sensors[i].p<Emin) {t2[i]=1;thoidiemchet[i]=(sensors[i].energy-Emin)/sensors[i].p;}
	}
	for(int i=1;i<=n;i++) {
		if(t1[i]==1||t2[i]==1) sonutchet++;
		if(thoidiemchet[i]<thoidiemchetdautien) thoidiemchetdautien=thoidiemchet[i];
    }
    
    ob.soluongnutchet=sonutchet;    
    ob.thoidiemchetdautien=thoidiemchetdautien;
    ob.soluongnutchet=sonutchet;
    ob.tdichuyen=dichuyen;
    ob.tsac=sac;
    
    
    
    //ob.fitness=ob.soluongnutchet/n+abs(Pm*ob.tdichuyen)/Emc;
    ob.fitness=ob.soluongnutchet/n-abs(U*ob.tsac)/Emc;
    ob.chargetime=xx.chargetime;
    ob.tsac=xx.tsac;
    return ob;
}
Individual random_key_encoding (){
	Individual individual;
	vector<vector<double> > ran_array;
	vector<double> qwe;
	double qq;
	qwe.push_back(0);
	for(int i=1;i<=n;i++) {
		qq=double_rand(0,1);
		qwe.push_back(qq);
	}
	qwe.push_back(0);
	individual.mhpath=qwe;
	individual.path=encode(individual.mhpath);
    individual=init_fuzzy(individual);
    individual=init_fitness(individual);
	return individual;
}
Individual polymute(Individual p) {
	double u,phi;
	for(int i=1;i<=n;i++) {
		u=double_rand(0.00000000000000000001,1-0.00000000000000000001);
		if(u<=0.5)  {
	        phi=pow((double)(2*u),(double)1/(nm+1))-1;
	    	p.mhpath[i]=p.mhpath[i]*(phi+1);
	    }
	    else {
	    phi=1-pow((double)(2*(1-u)),(double)1/(nm+1));
		p.mhpath[i]=p.mhpath[i]+phi*(1-p.mhpath[i]);
	}
	}
	p.path=encode(p.mhpath);
    p=init_fuzzy(p);
    p=init_fitness(p);
	return p;
}
vector<Individual> sbx(Individual p1,Individual p2) {
	vector<Individual> child(2);
	double u=double_rand(0.00000000000000000001,1-0.00000000000000000001);
	double beta;
	if(u<=0.5) beta=pow((double)(2.0*u),(double)1/(nc+1)); else beta=pow((double)(0.5/(1-u)),(double)1/(nc+1));
	child[0].mhpath.push_back(0);
	child[1].mhpath.push_back(0);
	for(int i=1;i<=n;i++) {
		child[0].mhpath.push_back((p1.mhpath[i]*(1+beta)+p2.mhpath[i]*(1-beta))*0.5);
		child[1].mhpath.push_back((p2.mhpath[i]*(1+beta)+p1.mhpath[i]*(1-beta))*0.5);
	}
	child[0].mhpath.push_back(0);
	child[1].mhpath.push_back(0);
	child[0].path=encode(child[0].mhpath);
	child[1].path=encode(child[1].mhpath);
	child[0]=init_fuzzy(child[0]);
	child[1]=init_fuzzy(child[1]);
	child[0]=init_fitness(child[0]);
	child[1]=init_fitness(child[1]);
	return child;
}
void Create_Pop () {
collect.pop.clear();

for(int i=0;i<POP_SIZE;i++) collect.pop.push_back(random_key_encoding());
collect.best=collect.pop[0];
}
vector<Individual> reproduction(){
	vector<Individual> offspring;
	Individual parent1, parent2;
	int m1,m2,p1,p2;
	while (offspring.size() < POP_SIZE-1){
		m1=random(0,POP_SIZE-1);
		m2=random(0,POP_SIZE-1);
		if(collect.pop[m2].fitness<collect.pop[m1].fitness) m1=m2;
		do{
		p1=random(0,POP_SIZE-1);
		p2=random(0,POP_SIZE-1);
		if(collect.pop[p2].fitness<collect.pop[p1].fitness) p1=p2;
		} while(p1==m1);
		parent1=collect.pop[p1];
		parent2=collect.pop[m1];
		if (double_rand(0, 1) <= CROSSOVER_RATE){
		vector<Individual> child = sbx(parent1, parent2);
		if(double_rand(0,1)<MUTATION_RATE) {
			child[0]=polymute(child[0]);
		}
		if(double_rand(0,1)<MUTATION_RATE) {
			child[1]=polymute(child[1]);
		}
		offspring.push_back(child[0]);
		offspring.push_back(child[1]);
		}
	}
	return offspring;
}
Individual solve() {
	Create_Pop();
	int gen = 0;	
	while (gen++ < GENERATIONS){
		int best=0;
		for(int i=1;i<POP_SIZE;i++) {
			if(collect.pop[i].fitness<collect.pop[best].fitness) best=i;
		}
		collect.best=collect.pop[best];
		vector<Individual> offspring=reproduction();
		offspring.push_back(collect.pop[best]);
		collect.pop=offspring;
	}
	return collect.best;
}
void init2() {
	double tongthoigiansac=min((Emc-collect.best.tdichuyen*Pm)/U,T-collect.best.tdichuyen);
	if(tongthoigiansac<0) tongthoigiansac=0;
	
	collect.best.tsac=tongthoigiansac;
	double conlai;
	for(int i=0;i<collect.pop.size();i++) {
		collect.pop[i]=collect.best;
		conlai=tongthoigiansac;
		if(i==0) continue;
		for(int j=1;j<collect.pop[i].chargetime.size()-1;j++) {
           double cantren=conlai;
           if(cantren>(Emax-Emin)/(U-sensors[collect.pop[i].path[j]].p)) cantren=(Emax-Emin)/(U-sensors[collect.pop[i].path[j]].p);
           if(cantren<0) cantren=0;
		   double t=double_rand(0,cantren);
           collect.pop[i].chargetime[j]=t;
           conlai-=(t);
		}
		collect.pop[i]=init_fitness(collect.pop[i]);
	}
	return ;
}
Individual mut2(Individual a) {
	int i=random(1,a.chargetime.size()-2);
	int j,uu=0;
	do {uu++;
		j=random(1,a.chargetime.size()-2);
		if(uu=10) break;
	} while (i==j);
	double dieuchinh1=(double_rand(0,(Emax-Emin)/(U-sensors[a.path[i]].p)-a.chargetime[i])),dieuchinh2=(double_rand(0,a.chargetime[j]));
	if(dieuchinh1>dieuchinh2) dieuchinh1=dieuchinh2;
	a.chargetime[i]+=dieuchinh1;
	a.chargetime[j]-=dieuchinh1;
	a=init_fitness(a);
	return a;
}
int kqq=0;
vector<Individual> SPAH (Individual a, Individual b) {
	if(kqq==1) {
		//a.check();
	//	b.check();
	}
	Individual child1=a,child2=b;
	int point=random(1,a.path.size()-2);
	double gr=(Emc-a.tdichuyen*Pm)/U;
	double beta=double_rand(-0.5,0.5);
	for(int i=point+1;i<=a.path.size()-2;i++) {
		child1.chargetime[i]=b.chargetime[i];
		child2.chargetime[i]=a.chargetime[i];
	}
	child1.chargetime[point]=abs((1-beta)*a.chargetime[point]+beta*(b.chargetime[point]));
	child2.chargetime[point]=abs((beta)*a.chargetime[point]+(1-beta)*(b.chargetime[point]));
	double t1=0,t2=0;
	for(int i=1;i<=a.chargetime.size()-2;i++) {
		t1+=child1.chargetime[i];
		t2+=child2.chargetime[i];
	}
	if(t1>gr) {
		double heso=t1/(gr);
		for(int i=1;i<=child1.chargetime.size()-2;i++) child1.chargetime[i]/=heso;
	} 
	else {
		double plus=gr-t1;
		for(int i=1;i<=child1.chargetime.size()-2&&plus>0;i++) {
			double t=double_rand(0,(Emax-Emin)/(U-sensors[child1.path[i]].p)-child1.chargetime[i]);
			if(t>plus) {
				t=plus;
			}
			child1.chargetime[i]+=t;
			plus-=t;
		}
	}
	if(t2>gr) {
		double heso=t2/(gr);
		for(int i=1;i<=child2.chargetime.size()-2;i++) child2.chargetime[i]/=heso;
	} 
	else {
		double plus=gr-t2;
		for(int i=1;i<=child2.chargetime.size()-2&&plus>0;i++) {
			double t=double_rand(0,(Emax-Emin)/(U-sensors[child2.path[i]].p)-child2.chargetime[i]);
			if(t>plus) {
				t=plus;
			}
			child2.chargetime[i]+=t;
			plus-=t;
		}
	}
	child1=init_fitness(child1);
	child2=init_fitness(child2);
	vector<Individual> q;
	q.push_back(child1);
	q.push_back(child2);
	return q;
}
vector<Individual> sbx2(Individual p1,Individual p2) {
	double gr=(Emc-p1.tdichuyen*Pm)/U;
	vector<Individual> child(2);
	child[0]=p1;
	child[1]=p2;
	child[0].chargetime.clear();
	child[1].chargetime.clear();
	child[0].chargetime.resize(0);
	child[1].chargetime.resize(0);
	double u=double_rand(0.0000000000001,1-0.00000000000001);
	double beta;
	if(u<=0.5) beta=pow((double)(2.0*u),(double)1/(nc2+1)); else beta=pow((double)(0.5/(1-u)),(double)1/(nc2+1));
	child[0].chargetime.push_back(0);
	child[1].chargetime.push_back(0);
	for(int i=1;i<=p1.path.size()-2;i++) {
		child[0].chargetime.push_back((p1.chargetime[i]*(1+beta)+p2.chargetime[i]*(1-beta))*0.5);
		child[1].chargetime.push_back((p2.chargetime[i]*(1+beta)+p1.chargetime[i]*(1-beta))*0.5);
	}
	child[0].chargetime.push_back(0);
	child[1].chargetime.push_back(0);
	double t1=0,t2=0;
	for(int i=1;i<=p1.chargetime.size()-2;i++) {
		t1+=child[0].chargetime[i];
		t2+=child[1].chargetime[i];
	}
	if(t1>gr) {
		double heso=t1/(gr);
		for(int i=1;i<=child[0].chargetime.size()-2;i++) child[0].chargetime[i]/=heso;
	} 
	else {
		double plus=gr-t1;
		for(int i=1;i<=child[0].chargetime.size()-2&&plus>0;i++) {
			double t=double_rand(0,abs((Emax-Emin)/(U-sensors[child[0].path[i]].p)-child[0].chargetime[i]));
			if(i==child[0].chargetime.size()-2) t=plus;
			if(t>plus) {
				t=plus;
			}
			child[0].chargetime[i]+=t;
			plus-=t;
		}
	}
	if(t2>gr) {
		double heso=t2/(gr);
		for(int i=1;i<=child[1].chargetime.size()-2;i++) child[1].chargetime[i]/=heso;
	} 
	else {
		double plus=gr-t2;
		for(int i=1;i<=child[1].chargetime.size()-2&&plus>0;i++) {
			double t=double_rand(0,abs((Emax-Emin)/(U-sensors[child[1].path[i]].p)-child[1].chargetime[i]));
			if(i==child[1].chargetime.size()-2) t=plus;
			if(t>plus) {
				t=plus;
			}
			child[1].chargetime[i]+=t;
			plus-=t;
		}
	}
	child[0]=init_fitness(child[0]);
	child[1]=init_fitness(child[1]);
	return child;
}
Individual polymute2(Individual p) {
	double u,phi;
	double gr=(Emc-p.tdichuyen*Pm)/U;
	for(int i=1;i<=p.chargetime.size()-2;i++) {
		u=double_rand(0.000000000001,1-0.0000000000000001);
		if(u<=0.5)  {
	        phi=pow((double)(2*u),(double)1/(nm+1))-1;
	    	p.chargetime[i]=p.chargetime[i]*(phi+1);
	    }
	    else {
	    phi=1-pow((double)(2*(1-u)),(double)1/(nm+1));
		p.chargetime[i]=p.chargetime[i]+phi*(1-p.chargetime[i]);
	}
	}
	double t2=0;
	for(int i=1;i<=p.chargetime.size()-2;i++) {
		t2+=p.chargetime[i];
	}
	if(t2>gr) {
		double heso=t2/(gr);
		for(int i=1;i<=p.chargetime.size()-2;i++) p.chargetime[i]/=heso;
	} 
	else {
		double plus=gr-t2;
		for(int i=1;i<=p.chargetime.size()-2&&plus>0;i++) {
			double t=double_rand(0,abs((Emax-Emin)/(U-sensors[p.path[i]].p)-p.chargetime[i]));
			if(i==p.chargetime.size()-2) t=plus;
			if(t>plus) {
				t=plus;
			}
			p.chargetime[i]+=t;
			plus-=t;
		}
	}
    p=init_fitness(p);
	return p;
}

void reproduction2() {
	Individual parent1, parent2;
	int m1,p1;
	for(int i=0;i<POP_SIZE/2;i++) {
		m1=random(0,POP_SIZE-1);
		do{
		p1=random(0,POP_SIZE-1);
		} while(p1==m1);
		parent1=collect.pop[p1];
		parent2=collect.pop[m1];
		if (double_rand(0, 1) <= 1){
		vector<Individual> child = SPAH(parent1, parent2);
		if(double_rand(0,1)<MUTATION_RATE2) {
			child[0]=mut2(child[0]);
		}
		if(double_rand(0,1)<MUTATION_RATE2) {
			child[1]=mut2(child[1]);
		}
		collect.pop.push_back(child[0]);
		collect.pop.push_back(child[1]);
	}
    }
}
Individual solve2() {
	int gen = 0;	
	while (gen++<GENERATIONSt){
		for(int i=0;i<collect.pop.size();i++) {
			for(int j=i+1;j<collect.pop.size();j++) {
				if(collect.pop[j].fitness<collect.pop[i].fitness) swap(collect.pop[i],collect.pop[j]);
			}
		}
		collect.pop.resize(POP_SIZE);
		collect.best=collect.pop[0];
		reproduction2();
	}
	return collect.best;
}
void update(Individual solution, double T) {
	for(int i=1;i<solution.path.size()-1;i++) {
		sensors[solution.path[i]].energy+=U*solution.chargetime[i];
	}
	double max_p=0;
	for(int i=1;i<=n;i++) {
		sensors[i].energy-=(T)*sensors[i].p;
		if(sensors[i].p>max_p&&sensors[i].energy>Emin) max_p=sensors[i].p;
	}
    
	for(int i=1;i<=n;i++) sensors[i].hesosac=sensors[i].p/(max_p*1.05);
	Ttongthe-=(T);
	cout<<T<<endl<<Ttongthe<<endl;
}
int main() { 
    // ten file chay se duoc luu trong mang tenfile[], file he so sac chay tuong ung se luu trong mang hesosac[]. Phan tu dau tien la tenfile[1]
    DIR *dpdf;
    struct dirent *epdf;
    dpdf=opendir("dulieu");
    int ks=0,i=1;
    if (dpdf !=NULL){
         while (epdf=readdir(dpdf)){
         	if(ks++<2) continue;
         	tenfile[i]="dulieu/"+(string) epdf->d_name;
         	hesosac[i]=(string)"hesosac/"+(string)"cr_"+(string) epdf->d_name;
         	i++;
        }
    }
    closedir(dpdf);
    
    //cac file ket qua
    fstream fout1,fout2,fout3,fout4;                      
    string fileghi1="Fuzzy-GA/seed=1-10.txt";
    string fileghi2="Fuzzy-GA/nangluongdichuyen=1-10.txt";
    string fileghi3="Fuzzy-GA/nangluongsac.txt";
    string fileghi4="Fuzzy-GA/cantren.txt";
    fout1.open(fileghi1.c_str(),ios::out);
	fout2.open(fileghi2.c_str(),ios::out);
	fout3.open(fileghi3.c_str(),ios::out);
	fout4.open(fileghi4.c_str(),ios::out);

	
	//cac mang gia tri chay thuc nghiem neu co
	double E[5]={0.5,1, 1.5, 2.0, 2.5};
	
	//vong for chay thuc nghien
	for(int vol=0;vol<=0;vol=vol+1) {
	//vong for chay tung file   
	    for(int i=5;i<=5;i++) {
	    	//vong for chay tung seed
	    	double avgnode = 0, avgmove = 0, avgcharge = 0, cantrenn = 0, num_seed = 1;
		    for(int seed=1;seed<=num_seed;seed++) {
		    	//pha 1
                readfile(i,seed, 1);
                U = 1 ;
                Emc = 216000 ;
                Ttongthe = 200000;
                int cantren=0;
                for(int itt=1;itt<=n;itt++) {
                    if(sensors[itt].energy-sensors[itt].p*Ttongthe<Emin) cantren++;
                }
                cantrenn = cantren;
                Individual solution;
                double dichuyeen=0, sacc = 0;
                
                while(Ttongthe>1) {
                T=min(Ttongthe,20000.0);
                //dieu chinh he so sac
                double anpha=0.8,beta=0.2,gamma=0.1;
                int canduoi=0,cantren=0;
                for(int itt=1;itt<=n;itt++) {
            	    if((sensors[itt].energy-Emin)/sensors[itt].p>=T&&Ttongthe>5000) sensors[itt].xacsuatsac=beta+(1-beta)*sensors[itt].hesosac;
            	    else {
            		    if(sensors[itt].energy+(Emax-Emin)-sensors[itt].p*T<Emin||sensors[itt].energy<Emin) {sensors[itt].xacsuatsac=gamma*sensors[itt].hesosac;canduoi++;}
            		    else sensors[itt].xacsuatsac=anpha+(1-anpha)*sensors[itt].hesosac;
                    }
                    if(sensors[itt].energy-sensors[itt].p*T<Emin) cantren++;
                }
                //pha 2
                solution = solve();
                solution=output(solution);
                if(solution.tdichuyen>1) {
                //pha 3
                init2();
                
                solution=solve2();

                solution=output(solution);
                }
                else {
                	for(int i=1;i<=n;i++) solution.chargetime[i]=0;
                	solution.tsac=0;
                	solution.tdichuyen=Ttongthe;
                	solution=output(solution);
                	solution.tdichuyen=Ttongthe;
                }
                //check solution hop le
                solution.check();
                dichuyeen+=solution.tdichuyen;
                sacc += solution.tsac;
                update(solution, T);
                cout<<Ttongthe<<endl;
                if(Ttongthe<=1) {break;}
                }
                //in file
                int qq = 0;
				for(int i=1;i<=n;i++) {
					if(sensors[i].energy<=Emin+50) qq++;
				}
                avgnode += qq;
                avgmove += dichuyeen * Pm;
                avgcharge += sacc * U;
                }

            fout1<<(avgnode / num_seed )<<'\n';
            fout2<<(avgmove / num_seed)<<'\n';
            fout3<<(avgcharge / num_seed)<<'\n';
            fout4<<cantrenn<<'\n';
			}
		}
	fout1.close();
	fout2.close();
	fout3.close();
	fout4.close();
	int qq = 0;
	for(int i=1;i<=n;i++) {
		if(sensors[i].energy<=Emin) qq++;
	}
	cout<<endl<<qq;
   cout<<endl<<"end";
    return 0;
}
